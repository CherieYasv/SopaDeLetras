package com.example.vista;

import java.util.Scanner;

import com.example.modelo.Resultado;

public class VistaConsola implements Vista {

    private final Scanner teclado = new Scanner(System.in);

    @Override
    public int mostrarMenu() {
        System.out.println();
        System.out.println("===== SOPITA DE LETRONAS =====");
        System.out.println("1. Buscar una palabra");
        System.out.println("2. Generar una sopa nueva");
        System.out.println("3. Resolver (buscar Gregorio, Coronel y Macondo)");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
        return leerEntero();
    }

    @Override
    public String pedirPalabra() {
        System.out.print("Palabra a buscar: ");
        return teclado.nextLine().trim();
    }

    @Override
    public String pedirArchivo(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    @Override
    public int pedirTamano() {
        System.out.print("Tamaño de la cuadrícula (ej. 12 para 12x12): ");
        return leerEntero();
    }

    @Override
    public void mostrarCuadricula(String[][] celdas) {
        int columnas = celdas[0].length;
        int ancho = String.valueOf(Math.max(celdas.length, columnas)).length() + 1;
        String formato = "%" + ancho + "s";

        System.out.println();
        System.out.printf(formato, "");
        for (int c = 1; c <= columnas; c++) {
            System.out.printf(formato, c);
        }
        System.out.println();

        for (int r = 0; r < celdas.length; r++) {
            System.out.printf(formato, r + 1);
            for (String letra : celdas[r]) {
                System.out.printf(formato, letra);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void mostrarResultado(String palabra, Resultado r) {
        if (r.encontrada()) {
            System.out.println("\"" + palabra + "\" SÍ está. Empieza en renglón " + r.renglon()
                    + ", columna " + r.columna() + " y va hacia " + r.direccion() + ".");
        } else {
            System.out.println("\"" + palabra + "\" NO está en la cuadrícula.");
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) { System.out.println(mensaje); }

    @Override
    public void mostrarError(String mensaje) { System.out.println("Error: " + mensaje); }

    private int leerEntero() {
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
