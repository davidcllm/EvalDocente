package evaldocente.service;

import evaldocente.model.*;
import evaldocente.repository.DataRepository;

import java.time.LocalDateTime;
import java.util.*;

public class AdminService {

    private final DataRepository repo = DataRepository.getInstance();

    // --- Gestión de Docentes ---

    public ResultadoOperacion crearDocente(String id, String nombre, String departamento) {
        if (repo.buscarDocente(id).isPresent()) {
            return new ResultadoOperacion(false, "Ya existe un docente con ese ID.");
        }
        Docente d = new Docente(id, nombre, departamento);
        repo.guardarDocente(d);
        return new ResultadoOperacion(true, "Docente '" + nombre + "' creado correctamente.");
    }

    public Collection<Docente> listarDocentes() { return repo.todosDocentes(); }

    // --- Gestión de Materias ---

    public ResultadoOperacion crearMateria(String id, String nombre, String carrera, int semestre, String docenteId) {
        if (repo.buscarMateria(id).isPresent()) {
            return new ResultadoOperacion(false, "Ya existe una materia con ese ID.");
        }
        if (repo.buscarDocente(docenteId).isEmpty()) {
            return new ResultadoOperacion(false, "No existe un docente con ID: " + docenteId);
        }
        Materia m = new Materia(id, nombre, carrera, semestre, docenteId);
        repo.guardarMateria(m);
        repo.buscarDocente(docenteId).ifPresent(d -> d.agregarMateria(id));
        return new ResultadoOperacion(true, "Materia '" + nombre + "' creada correctamente.");
    }

    public Collection<Materia> listarMaterias() { return repo.todasMaterias(); }

    // --- Gestión de Formularios ---

    public ResultadoOperacion crearFormulario(String titulo, String periodo,
                                               String docenteId, String materiaId,
                                               LocalDateTime inicio, LocalDateTime fin) {
        if (repo.buscarDocente(docenteId).isEmpty()) {
            return new ResultadoOperacion(false, "Docente no encontrado.");
        }
        if (repo.buscarMateria(materiaId).isEmpty()) {
            return new ResultadoOperacion(false, "Materia no encontrada.");
        }
        if (fin.isBefore(inicio)) {
            return new ResultadoOperacion(false, "La fecha de fin no puede ser anterior a la de inicio.");
        }

        String id = "F" + System.currentTimeMillis();
        Formulario f = new Formulario(id, titulo, periodo, docenteId, materiaId, inicio, fin);

        // Agregar preguntas estándar predefinidas
        agregarPreguntasEstandar(f);

        repo.guardarFormulario(f);
        return new ResultadoOperacion(true, "Formulario creado con ID: " + id);
    }

    private void agregarPreguntasEstandar(Formulario f) {
        String[][] preguntasEscala = {
            {"PE1", "El docente domina el tema impartido", "DOCENTE"},
            {"PE2", "El docente explica con claridad los conceptos", "DOCENTE"},
            {"PE3", "El docente muestra disposición para resolver dudas", "DOCENTE"},
            {"PE4", "El docente cumple puntualmente con los horarios", "DOCENTE"},
            {"PE5", "Los contenidos del curso son relevantes para mi formación", "CURSO"},
            {"PE6", "Los materiales de apoyo son adecuados y suficientes", "CURSO"},
            {"PE7", "El ritmo del curso es apropiado", "CURSO"},
        };
        String[][] preguntasTexto = {
            {"PT1", "¿Qué aspectos del docente destacarías positivamente?", "DOCENTE"},
            {"PT2", "¿Qué áreas de mejora identificas en el curso?", "CURSO"},
        };

        for (String[] p : preguntasEscala) {
            Pregunta preg = crearOObtenerPregunta(p[0], p[1], Pregunta.TipoPregunta.ESCALA, p[2]);
            f.agregarPregunta(preg);
        }
        for (String[] p : preguntasTexto) {
            Pregunta preg = crearOObtenerPregunta(p[0], p[1], Pregunta.TipoPregunta.TEXTO_LIBRE, p[2]);
            f.agregarPregunta(preg);
        }
    }

    private Pregunta crearOObtenerPregunta(String id, String texto, Pregunta.TipoPregunta tipo, String categoria) {
        return repo.buscarPregunta(id).orElseGet(() -> {
            Pregunta p = new Pregunta(id, texto, tipo, categoria);
            repo.guardarPregunta(p);
            return p;
        });
    }

    public ResultadoOperacion modificarVentanaTiempo(String formularioId,
                                                      LocalDateTime nuevaInicio,
                                                      LocalDateTime nuevaFin) {
        return repo.buscarFormulario(formularioId).map(f -> {
            if (nuevaFin.isBefore(nuevaInicio)) {
                return new ResultadoOperacion(false, "La fecha fin no puede ser anterior al inicio.");
            }
            f.setFechaInicio(nuevaInicio);
            f.setFechaFin(nuevaFin);
            return new ResultadoOperacion(true, "Ventana de tiempo actualizada.");
        }).orElse(new ResultadoOperacion(false, "Formulario no encontrado."));
    }

    public Collection<Formulario> listarFormularios() { return repo.todosFormularios(); }

    // --- Gestión de Estudiantes ---

    public ResultadoOperacion crearEstudiante(String id, String nombre, String password,
                                               String matricula, String carrera, int semestre) {
        if (repo.buscarUsuario(id).isPresent()) {
            return new ResultadoOperacion(false, "Ya existe un usuario con ese ID.");
        }
        Estudiante e = new Estudiante(id, nombre, password, matricula, carrera, semestre);
        repo.guardarUsuario(e);
        return new ResultadoOperacion(true, "Estudiante '" + nombre + "' registrado.");
    }
}
