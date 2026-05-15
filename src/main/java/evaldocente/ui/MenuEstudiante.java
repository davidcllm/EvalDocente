package evaldocente.ui;

import evaldocente.model.*;
import evaldocente.repository.DataRepository;
import evaldocente.service.EvaluacionService;
import evaldocente.service.ResultadoOperacion;

import java.util.*;

public class MenuEstudiante {

    private final Estudiante estudiante;
    private final EvaluacionService evalService = EvaluacionService.getInstance();
    private final DataRepository repo = DataRepository.getInstance();

    public MenuEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void mostrar() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("MÓDULO ESTUDIANTE");

            System.out.printf(
                    "  Bienvenido/a: %s | Carrera: %s | Semestre: %d\n",
                    estudiante.getNombre(),
                    estudiante.getCarrera(),
                    estudiante.getSemestre()
            );

            ConsoleUI.separador();
            ConsoleUI.opcion(1, "Ver y contestar evaluaciones disponibles");
            ConsoleUI.opcion(2, "Ver mis evaluaciones completadas");
            ConsoleUI.opcion(0, "Cerrar sesión");
            ConsoleUI.separador();

            int op = ConsoleUI.leerEntero("Selecciona una opción", 0, 2);

            switch (op) {
                case 1 -> contestarEvaluaciones();
                case 2 -> verCompletadas();
                case 0 -> activo = false;
            }
        }
    }

    private void contestarEvaluaciones() {
        ConsoleUI.encabezado("EVALUACIONES DISPONIBLES");

        List<Formulario> disponibles = evalService.obtenerFormulariosDisponibles(estudiante);

        if (disponibles.isEmpty()) {
            ConsoleUI.info("No tienes evaluaciones pendientes en este momento.");
            ConsoleUI.pausa();
            return;
        }

        for (int i = 0; i < disponibles.size(); i++) {
            Formulario f = disponibles.get(i);

            String matNombre = repo.buscarMateria(f.getMateriaId())
                    .map(Materia::getNombre)
                    .orElse("?");

            String docNombre = repo.buscarDocente(f.getDocenteId())
                    .map(Docente::getNombre)
                    .orElse("?");

            ConsoleUI.opcion(
                    i + 1,
                    String.format(
                            "%s | Docente: %s | Materia: %s | Hasta: %s",
                            f.getTitulo(),
                            docNombre,
                            matNombre,
                            f.getFechaFin().toString().substring(0, 16)
                    )
            );
        }

        ConsoleUI.opcion(0, "Volver");
        ConsoleUI.separador();

        int op = ConsoleUI.leerEntero("Selecciona evaluación", 0, disponibles.size());

        if (op == 0) {
            return;
        }

        Formulario seleccionado = disponibles.get(op - 1);
        contestarFormulario(seleccionado);
    }

    private void contestarFormulario(Formulario formulario) {

        String matNombre = repo.buscarMateria(formulario.getMateriaId())
                .map(Materia::getNombre)
                .orElse("?");

        String docNombre = repo.buscarDocente(formulario.getDocenteId())
                .map(Docente::getNombre)
                .orElse("?");

        ConsoleUI.encabezado("EVALUACIÓN ANÓNIMA");

        System.out.println("  Esta evaluación es completamente anónima.");
        System.out.println("  Docente: " + docNombre);
        System.out.println("  Materia: " + matNombre);

        ConsoleUI.separador();

        Map<String, Object> respuestasMap = new LinkedHashMap<>();
        List<Pregunta> preguntas = formulario.getPreguntas();

        for (int i = 0; i < preguntas.size(); i++) {

            Pregunta p = preguntas.get(i);

            System.out.printf("\n  %d. %s\n", i + 1, p.getTexto());

            if (p.getTipo() == Pregunta.TipoPregunta.ESCALA) {

                System.out.println("     1=Muy malo  2=Malo  3=Regular  4=Bueno  5=Excelente");

                int valor = ConsoleUI.leerEntero("     Tu calificación", 1, 5);

                respuestasMap.put(p.getId(), valor);

            } else {

                String texto = ConsoleUI.leerLinea("     Tu comentario (Enter para omitir)");

                if (!texto.isEmpty()) {
                    respuestasMap.put(p.getId(), texto);
                }
            }
        }

        ConsoleUI.separador();

        System.out.println("\n  ¿Confirmas enviar tu evaluación?");

        ConsoleUI.opcion(1, "Sí, enviar");
        ConsoleUI.opcion(0, "Cancelar");

        int confirm = ConsoleUI.leerEntero("Opción", 0, 1);

        if (confirm == 1) {

            ResultadoOperacion resultado = evalService.registrarRespuesta(
                    estudiante,
                    formulario.getId(),
                    respuestasMap
            );

            if (resultado.isExitoso()) {
                ConsoleUI.exito(resultado.getMensaje());
            } else {
                ConsoleUI.error(resultado.getMensaje());
            }

        } else {

            ConsoleUI.info("Evaluación cancelada.");
        }

        ConsoleUI.pausa();
    }

    private void verCompletadas() {

        ConsoleUI.encabezado("MIS EVALUACIONES COMPLETADAS");

        List<String> completadas = estudiante.getEvaluacionesRealizadas();

        if (completadas.isEmpty()) {

            ConsoleUI.info("No has completado ninguna evaluación todavía.");

        } else {

            System.out.printf(
                    "  Has completado %d evaluación(es).\n",
                    completadas.size()
            );

            completadas.forEach(
                    c -> System.out.println("   • Formulario/Docente: " + c)
            );
        }

        ConsoleUI.pausa();
    }
}