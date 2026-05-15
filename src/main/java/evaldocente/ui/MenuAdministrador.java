package evaldocente.ui;

import evaldocente.model.*;
import evaldocente.pattern.BitacoraObserver;
import evaldocente.pattern.GeneradorReportes;
import evaldocente.repository.DataRepository;
import evaldocente.service.AdminService;
import evaldocente.service.ResultadoOperacion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

public class MenuAdministrador {

    private final AdminService adminService = new AdminService();
    private final DataRepository repo = DataRepository.getInstance();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void mostrar() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("MÓDULO ADMINISTRADOR");
            ConsoleUI.opcion(1, "Gestión de Docentes");
            ConsoleUI.opcion(2, "Gestión de Materias");
            ConsoleUI.opcion(3, "Gestión de Formularios");
            ConsoleUI.opcion(4, "Gestión de Estudiantes");
            ConsoleUI.opcion(5, "Reportes");
            ConsoleUI.opcion(6, "Bitácora del sistema");
            ConsoleUI.opcion(0, "Cerrar sesión");
            ConsoleUI.separador();
            int op = ConsoleUI.leerEntero("Selecciona opción", 0, 6);
            switch (op) {
                case 1 -> menuDocentes();
                case 2 -> menuMaterias();
                case 3 -> menuFormularios();
                case 4 -> menuEstudiantes();
                case 5 -> menuReportes();
                case 6 -> verBitacora();
                case 0 -> activo = false;
            }
        }
    }

    // ==================== DOCENTES ====================

    private void menuDocentes() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("GESTIÓN DE DOCENTES");
            ConsoleUI.opcion(1, "Listar docentes");
            ConsoleUI.opcion(2, "Registrar nuevo docente");
            ConsoleUI.opcion(0, "Volver");
            int op = ConsoleUI.leerEntero("Opción", 0, 2);
            switch (op) {
                case 1 -> listarDocentes();
                case 2 -> registrarDocente();
                case 0 -> activo = false;
            }
        }
    }

    private void listarDocentes() {
        ConsoleUI.encabezado("LISTA DE DOCENTES");
        Collection<Docente> docs = adminService.listarDocentes();
        if (docs.isEmpty()) { ConsoleUI.info("Sin docentes registrados."); }
        else docs.forEach(d -> System.out.printf("  %-8s %-30s %s\n", d.getId(), d.getNombre(), d.getDepartamento()));
        ConsoleUI.pausa();
    }

    private void registrarDocente() {
        ConsoleUI.encabezado("NUEVO DOCENTE");
        String id = ConsoleUI.leerLinea("ID (ej: D010)");
        String nombre = ConsoleUI.leerLinea("Nombre completo");
        String depto = ConsoleUI.leerLinea("Departamento");
        ResultadoOperacion r = adminService.crearDocente(id, nombre, depto);
        if (r.isExitoso()) ConsoleUI.exito(r.getMensaje()); else ConsoleUI.error(r.getMensaje());
        ConsoleUI.pausa();
    }

    // ==================== MATERIAS ====================

    private void menuMaterias() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("GESTIÓN DE MATERIAS");
            ConsoleUI.opcion(1, "Listar materias");
            ConsoleUI.opcion(2, "Registrar nueva materia");
            ConsoleUI.opcion(0, "Volver");
            int op = ConsoleUI.leerEntero("Opción", 0, 2);
            switch (op) {
                case 1 -> listarMaterias();
                case 2 -> registrarMateria();
                case 0 -> activo = false;
            }
        }
    }

    private void listarMaterias() {
        ConsoleUI.encabezado("LISTA DE MATERIAS");
        Collection<Materia> mats = adminService.listarMaterias();
        if (mats.isEmpty()) { ConsoleUI.info("Sin materias registradas."); }
        else mats.forEach(m -> System.out.printf("  %-8s %-28s %-24s Sem:%d\n",
                m.getId(), m.getNombre(), m.getCarrera(), m.getSemestre()));
        ConsoleUI.pausa();
    }

    private void registrarMateria() {
        ConsoleUI.encabezado("NUEVA MATERIA");
        listarDocentes();
        String id = ConsoleUI.leerLinea("ID materia (ej: M010)");
        String nombre = ConsoleUI.leerLinea("Nombre de la materia");
        String carrera = ConsoleUI.leerLinea("Carrera");
        int semestre = ConsoleUI.leerEntero("Semestre", 1, 12);
        String docenteId = ConsoleUI.leerLinea("ID del docente");
        ResultadoOperacion r = adminService.crearMateria(id, nombre, carrera, semestre, docenteId);
        if (r.isExitoso()) ConsoleUI.exito(r.getMensaje()); else ConsoleUI.error(r.getMensaje());
        ConsoleUI.pausa();
    }

    // ==================== FORMULARIOS ====================

    private void menuFormularios() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("GESTIÓN DE FORMULARIOS");
            ConsoleUI.opcion(1, "Listar formularios");
            ConsoleUI.opcion(2, "Crear formulario");
            ConsoleUI.opcion(3, "Modificar ventana de tiempo");
            ConsoleUI.opcion(0, "Volver");
            int op = ConsoleUI.leerEntero("Opción", 0, 3);
            switch (op) {
                case 1 -> listarFormularios();
                case 2 -> crearFormulario();
                case 3 -> modificarVentana();
                case 0 -> activo = false;
            }
        }
    }

    private void listarFormularios() {
        ConsoleUI.encabezado("LISTA DE FORMULARIOS");
        Collection<Formulario> forms = adminService.listarFormularios();

        if (forms.isEmpty()) {
            ConsoleUI.info("Sin formularios.");
        } else {
            forms.forEach(f -> {
                String estado;

                if (f.estaActivo()) {
                    estado = "ACTIVO";
                } else if (f.getEstado() == Formulario.EstadoFormulario.PENDIENTE) {
                    estado = "PENDIENTE";
                } else {
                    estado = "FINALIZADO";
                }

                System.out.printf(
                        "  %-12s %-36s Estado:%-12s Inicio:%s  Fin:%s  Resp:%d\n",
                        f.getId(),
                        f.getTitulo(),
                        estado,
                        f.getFechaInicio().toString().substring(0, 16),
                        f.getFechaFin().toString().substring(0, 16),
                        f.getTotalRespuestas()
                );
            });
        }

        ConsoleUI.pausa();
    }

    private void crearFormulario() {
        ConsoleUI.encabezado("NUEVO FORMULARIO");
        listarDocentes();
        listarMaterias();
        String titulo = ConsoleUI.leerLinea("Título del formulario");
        String periodo = ConsoleUI.leerLinea("Periodo (ej: 2025-1)");
        String docenteId = ConsoleUI.leerLinea("ID del docente");
        String materiaId = ConsoleUI.leerLinea("ID de la materia");

        LocalDateTime inicio = leerFecha("Fecha inicio (yyyy-MM-dd HH:mm)");
        LocalDateTime fin = leerFecha("Fecha fin    (yyyy-MM-dd HH:mm)");

        ResultadoOperacion r = adminService.crearFormulario(titulo, periodo, docenteId, materiaId, inicio, fin);
        if (r.isExitoso()) ConsoleUI.exito(r.getMensaje()); else ConsoleUI.error(r.getMensaje());
        ConsoleUI.pausa();
    }

    private void modificarVentana() {
        ConsoleUI.encabezado("MODIFICAR VENTANA DE TIEMPO");
        listarFormularios();
        String id = ConsoleUI.leerLinea("ID del formulario");
        LocalDateTime inicio = leerFecha("Nueva fecha inicio (yyyy-MM-dd HH:mm)");
        LocalDateTime fin = leerFecha("Nueva fecha fin    (yyyy-MM-dd HH:mm)");
        ResultadoOperacion r = adminService.modificarVentanaTiempo(id, inicio, fin);
        if (r.isExitoso()) ConsoleUI.exito(r.getMensaje()); else ConsoleUI.error(r.getMensaje());
        ConsoleUI.pausa();
    }

    private LocalDateTime leerFecha(String prompt) {
        while (true) {
            String s = ConsoleUI.leerLinea(prompt);
            try {
                return LocalDateTime.parse(s, FMT);
            } catch (DateTimeParseException e) {
                ConsoleUI.error("Formato inválido. Usa: yyyy-MM-dd HH:mm");
            }
        }
    }

    // ==================== ESTUDIANTES ====================

    private void menuEstudiantes() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("GESTIÓN DE ESTUDIANTES");
            ConsoleUI.opcion(1, "Listar estudiantes");
            ConsoleUI.opcion(2, "Registrar estudiante");
            ConsoleUI.opcion(0, "Volver");
            int op = ConsoleUI.leerEntero("Opción", 0, 2);
            switch (op) {
                case 1 -> listarEstudiantes();
                case 2 -> registrarEstudiante();
                case 0 -> activo = false;
            }
        }
    }

    private void listarEstudiantes() {
        ConsoleUI.encabezado("LISTA DE ESTUDIANTES");
        repo.todosUsuarios().stream()
                .filter(u -> u instanceof Estudiante)
                .map(u -> (Estudiante) u)
                .forEach(e -> System.out.printf("  %-8s %-25s %-26s Sem:%d  Evals:%d\n",
                        e.getId(), e.getNombre(), e.getCarrera(),
                        e.getSemestre(), e.getEvaluacionesRealizadas().size()));
        ConsoleUI.pausa();
    }

    private void registrarEstudiante() {
        ConsoleUI.encabezado("NUEVO ESTUDIANTE");
        String id = ConsoleUI.leerLinea("ID (ej: E010)");
        String nombre = ConsoleUI.leerLinea("Nombre completo");
        String password = ConsoleUI.leerLinea("Contraseña");
        String matricula = ConsoleUI.leerLinea("Matrícula");
        String carrera = ConsoleUI.leerLinea("Carrera");
        int semestre = ConsoleUI.leerEntero("Semestre", 1, 12);
        ResultadoOperacion r = adminService.crearEstudiante(id, nombre, password, matricula, carrera, semestre);
        if (r.isExitoso()) ConsoleUI.exito(r.getMensaje()); else ConsoleUI.error(r.getMensaje());
        ConsoleUI.pausa();
    }

    // ==================== REPORTES ====================

    private void menuReportes() {
        boolean activo = true;
        while (activo) {
            ConsoleUI.encabezado("REPORTES");
            ConsoleUI.opcion(1, "Reporte por Docente");
            ConsoleUI.opcion(2, "Reporte por Materia");
            ConsoleUI.opcion(3, "Reporte por Carrera");
            ConsoleUI.opcion(4, "Reporte por Semestre");
            ConsoleUI.opcion(0, "Volver");
            int op = ConsoleUI.leerEntero("Opción", 0, 4);
            if (op == 0) { activo = false; continue; }

            GeneradorReportes gen = new GeneradorReportes();
            String parametro = "";

            switch (op) {
                case 1 -> {
                    gen.setEstrategia(GeneradorReportes.POR_DOCENTE);
                    listarDocentes();
                    parametro = ConsoleUI.leerLinea("ID del docente");
                }
                case 2 -> {
                    gen.setEstrategia(GeneradorReportes.POR_MATERIA);
                    listarMaterias();
                    parametro = ConsoleUI.leerLinea("ID de la materia");
                }
                case 3 -> {
                    gen.setEstrategia(GeneradorReportes.POR_CARRERA);
                    parametro = ConsoleUI.leerLinea("Nombre de la carrera");
                }
                case 4 -> {
                    gen.setEstrategia(GeneradorReportes.POR_SEMESTRE);
                    parametro = String.valueOf(ConsoleUI.leerEntero("Semestre", 1, 12));
                }
            }

            System.out.println(gen.generar(parametro));
            ConsoleUI.pausa();
        }
    }

    // ==================== BITÁCORA ====================

    private void verBitacora() {
        ConsoleUI.encabezado("BITÁCORA DEL SISTEMA");
        java.util.List<String> bitacora = BitacoraObserver.getBitacora();
        if (bitacora.isEmpty()) {
            ConsoleUI.info("La bitácora está vacía.");
        } else {
            bitacora.forEach(e -> System.out.println("  " + e));
        }
        ConsoleUI.pausa();
    }
}
