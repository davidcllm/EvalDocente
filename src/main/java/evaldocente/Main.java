package evaldocente;

import evaldocente.model.Administrador;
import evaldocente.model.Estudiante;
import evaldocente.model.Usuario;
import evaldocente.repository.DataRepository;
import evaldocente.ui.ConsoleUI;
import evaldocente.ui.MenuAdministrador;
import evaldocente.ui.MenuEstudiante;
import evaldocente.util.DataSeeder;

import java.util.Optional;

/**
 * Punto de entrada del Sistema de Retroalimentación Docente.
 *
 * Patrones implementados:
 *   - Singleton   : DataRepository, EvaluacionService
 *   - Observer    : BitacoraObserver notificado ante nueva respuesta
 *   - Strategy    : GeneradorReportes intercambia algoritmo de reporte
 */
public class Main {

    public static void main(String[] args) {
        mostrarBienvenida();
        DataSeeder.cargarDatosDemo();
        bucleLogin();
    }

    private static void mostrarBienvenida() {

        System.out.println();

        System.out.println(
                "  ╔══════════════════════════════════════════════════════════╗\n" +
                        "  ║    SISTEMA DE RETROALIMENTACIÓN DOCENTE ANÓNIMA          ║\n" +
                        "  ║    Educación Superior — v1.0                             ║\n" +
                        "  ╚══════════════════════════════════════════════════════════╝"
        );

        System.out.println();
    }

    private static void bucleLogin() {

        DataRepository repo = DataRepository.getInstance();

        boolean ejecutando = true;

        while (ejecutando) {

            ConsoleUI.encabezado("INICIO DE SESIÓN");

            ConsoleUI.opcion(1, "Iniciar sesión");
            ConsoleUI.opcion(0, "Salir del sistema");

            ConsoleUI.separador();

            int op = ConsoleUI.leerEntero("Opción", 0, 1);

            if (op == 0) {

                ConsoleUI.info("Saliendo del sistema. ¡Hasta pronto!");
                System.out.println();

                break;
            }

            String id = ConsoleUI.leerLinea("ID de usuario");
            String pass = ConsoleUI.leerLinea("Contraseña");

            Optional<Usuario> optUsuario = repo.autenticar(id, pass);

            if (optUsuario.isEmpty()) {

                ConsoleUI.error("Credenciales incorrectas. Intenta de nuevo.");
                ConsoleUI.pausa();

                continue;
            }

            Usuario usuario = optUsuario.get();

            ConsoleUI.exito(
                    "Sesión iniciada como: "
                            + usuario.getNombre()
                            + " ["
                            + usuario.getRol()
                            + "]"
            );

            if (usuario instanceof Administrador) {

                new MenuAdministrador().mostrar();

            } else if (usuario instanceof Estudiante) {

                new MenuEstudiante((Estudiante) usuario).mostrar();

            } else {

                ConsoleUI.error("Rol no reconocido.");
            }
        }
    }
}