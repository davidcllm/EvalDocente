package evaldocente.service;

import evaldocente.model.*;
import evaldocente.pattern.BitacoraObserver;
import evaldocente.pattern.Observable;
import evaldocente.pattern.Observer;
import evaldocente.repository.DataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio principal del módulo de evaluaciones.
 * Implementa Observable para notificar al sistema ante nuevas respuestas.
 */
public class EvaluacionService implements Observable {

    private static EvaluacionService instance;
    private final DataRepository repo = DataRepository.getInstance();
    private final List<Observer> observers = new ArrayList<>();

    private EvaluacionService() {
        // Registrar el observer de bitácora automáticamente
        agregarObserver(new BitacoraObserver());
    }

    public static EvaluacionService getInstance() {
        if (instance == null) instance = new EvaluacionService();
        return instance;
    }

    @Override
    public void agregarObserver(Observer o) { observers.add(o); }

    @Override
    public void eliminarObserver(Observer o) { observers.remove(o); }

    @Override
    public void notificarObservers(String evento, Object datos) {
        observers.forEach(o -> o.actualizar(evento, datos));
    }

    /**
     * Registra la respuesta de un estudiante a un formulario.
     * La respuesta es completamente anónima (no se guarda el ID del estudiante).
     */
    public ResultadoOperacion registrarRespuesta(Estudiante estudiante, String formularioId,
                                                  java.util.Map<String, Object> respuestasMap) {
        Optional<Formulario> optForm = repo.buscarFormulario(formularioId);
        if (optForm.isEmpty()) return new ResultadoOperacion(false, "Formulario no encontrado.");

        Formulario formulario = optForm.get();
        if (!formulario.estaActivo()) {
            return new ResultadoOperacion(false, "El formulario no está activo en este momento.");
        }

        // Verificar que el estudiante no haya evaluado ya este formulario
        String clave = formularioId + "_" + formulario.getDocenteId();
        if (estudiante.yaEvaluo(formularioId, formulario.getDocenteId())) {
            return new ResultadoOperacion(false, "Ya has completado esta evaluación anteriormente.");
        }

        // Crear respuesta ANÓNIMA (sin ID de estudiante)
        String respuestaId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Respuesta respuesta = new Respuesta(
                respuestaId,
                formularioId,
                formulario.getDocenteId(),
                formulario.getMateriaId(),
                estudiante.getCarrera(),
                estudiante.getSemestre()
        );

        respuestasMap.forEach(respuesta::agregarRespuesta);

        repo.guardarRespuesta(respuesta);
        formulario.incrementarRespuestas();
        estudiante.agregarEvaluacionRealizada(clave);

        notificarObservers("NUEVA_RESPUESTA", respuesta);
        return new ResultadoOperacion(true, "Evaluación registrada. ¡Gracias por tu retroalimentación!");
    }

    /**
     * Obtiene los formularios disponibles para un estudiante según carrera y semestre.
     */
    public List<Formulario> obtenerFormulariosDisponibles(Estudiante estudiante) {
        List<Formulario> disponibles = repo.formulariosParaEstudiante(
                estudiante.getCarrera(), estudiante.getSemestre());

        // Filtrar los que ya completó
        disponibles.removeIf(f -> estudiante.yaEvaluo(f.getId(), f.getDocenteId()));
        return disponibles;
    }
}
