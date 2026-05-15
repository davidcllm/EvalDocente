package evaldocente.ui;

import java.util.Scanner;

public class ConsoleUI {

    private static final Scanner scanner = new Scanner(System.in);

    public static void encabezado(String titulo) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.printf("║  %-58s║\n", titulo);
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    public static void separador() {
        System.out.println("──────────────────────────────────────────────────────────");
    }

    public static void exito(String msg) {
        System.out.println("  ✔  " + msg);
    }

    public static void error(String msg) {
        System.out.println("  ✖  " + msg);
    }

    public static void info(String msg) {
        System.out.println("  ℹ  " + msg);
    }

    public static void opcion(int num, String texto) {
        System.out.printf("  [%d] %s\n", num, texto);
    }

    public static String leerLinea(String prompt) {
        System.out.print("  → " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    public static int leerEntero(String prompt, int min, int max) {
        while (true) {
            try {
                String entrada = leerLinea(prompt + " (" + min + "-" + max + ")");
                int val = Integer.parseInt(entrada);
                if (val >= min && val <= max) return val;
                error("Ingresa un valor entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                error("Entrada inválida. Ingresa un número.");
            }
        }
    }

    public static void pausa() {
        System.out.print("\n  Presiona ENTER para continuar...");
        scanner.nextLine();
    }

    public static Scanner getScanner() {
        return scanner;
    }
}