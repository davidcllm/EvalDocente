package evaldocente.util;

import evaldocente.model.Administrador;
import evaldocente.repository.DataRepository;
import evaldocente.service.AdminService;

import java.time.LocalDateTime;

/**
 * Carga datos de demostración al inicio del sistema.
 */
public class DataSeeder {

    public static void cargarDatosDemo() {
        DataRepository repo = DataRepository.getInstance();
        AdminService admin = new AdminService();

        // Administrador
        repo.guardarUsuario(new Administrador("admin01", "Dr. Roberto Méndez", "admin123"));

        // Docentes
        admin.crearDocente("D001", "Dra. Ana Martínez", "Ciencias Básicas");
        admin.crearDocente("D002", "Ing. Carlos López", "Ingeniería de Software");
        admin.crearDocente("D003", "Mtra. Sandra Rojas", "Matemáticas");

        // Materias
        admin.crearMateria("M001", "Cálculo Diferencial",     "Ingeniería en Sistemas", 1, "D003");
        admin.crearMateria("M002", "Programación Orientada a Objetos", "Ingeniería en Sistemas", 3, "D002");
        admin.crearMateria("M003", "Bases de Datos",          "Ingeniería en Sistemas", 4, "D002");
        admin.crearMateria("M004", "Física I",                "Ingeniería Industrial",  1, "D001");
        admin.crearMateria("M005", "Álgebra Lineal",          "Ingeniería Industrial",  2, "D003");

        // Estudiantes
        admin.crearEstudiante("E001", "Luis Pérez",    "pass1", "A230001", "Ingeniería en Sistemas", 3);
        admin.crearEstudiante("E002", "María García",  "pass2", "A230002", "Ingeniería en Sistemas", 3);
        admin.crearEstudiante("E003", "Jorge Torres",  "pass3", "A230003", "Ingeniería Industrial",  1);
        admin.crearEstudiante("E004", "Ana Herrera",   "pass4", "A230004", "Ingeniería en Sistemas", 4);

        // Formularios (activos ahora)
        LocalDateTime ahora = LocalDateTime.now();
        admin.crearFormulario("Evaluación Docente - POO 2025-1",   "2025-1", "D002", "M002",
                ahora.minusDays(2), ahora.plusDays(5));
        admin.crearFormulario("Evaluación Docente - BD 2025-1",    "2025-1", "D002", "M003",
                ahora.minusDays(1), ahora.plusDays(7));
        admin.crearFormulario("Evaluación Docente - Física 2025-1","2025-1", "D001", "M004",
                ahora.minusDays(3), ahora.plusDays(4));
        // Formulario cerrado (demo)
        admin.crearFormulario("Evaluación Docente - Cálculo 2024-2", "2024-2", "D003", "M001",
                ahora.minusDays(60), ahora.minusDays(30));

        System.out.println("  [Sistema] Datos de demostración cargados correctamente.");
    }
}
