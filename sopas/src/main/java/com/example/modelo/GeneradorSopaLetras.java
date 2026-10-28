package com.example.modelo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Clase GeneradorSopaLetras se encarga de hacer la base y la lógica de la generación 
 * de la sopa de letras.
 * 
 * @author CAAA
 * @version 1
 * 
 */
public class GeneradorSopaLetras {

    private static final String ABECEDARIO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int INTENTOS_POR_PALABRA = 500;

    private final Random random = new Random();

    /*
     * Método que ejerce la lógica para elaborar la matriz de la sopa de letras.
     */
    public String[][] generar(int tamano, String[] palabras) {
        if (tamano < 1) {
            return null;
        }
        String[] lista = Arrays.stream(palabras)
                .map(p -> p.trim().toUpperCase())
                .sorted(Comparator.comparingInt(String::length).reversed()) // las largas primero
                .toArray(String[]::new);

        for (String p : lista) {
            if (p.length() > tamano) {
                return null;
            }
        }

        char[][] grid = new char[tamano][tamano];
        for (char[] fila : grid) {
            Arrays.fill(fila, '\0');
        }

        for (String p : lista) {
            if (!colocar(grid, p)) {
                return null;
            }
        }

        rellenar(grid);
        return aMatrizDeTexto(grid);
    }

    /*
     * Método que elabora la lógica de insertar una palabra correcto o incorrecta en la sopa de letras.
     */
    private boolean colocar(char[][] grid, String palabra) {
        Direccion[] direcciones = Direccion.values();
        for (int intento = 0; intento < INTENTOS_POR_PALABRA; intento++) {
            Direccion d = direcciones[random.nextInt(direcciones.length)];
            int r = random.nextInt(grid.length);
            int c = random.nextInt(grid.length);
            if (cabe(grid, palabra, r, c, d)) {
                for (int i = 0; i < palabra.length(); i++) {
                    grid[r + i * d.getDr()][c + i * d.getDc()] = palabra.charAt(i);
                }
                return true;
            }
        }
        return false;
    }

    /*
     * Método que evalua si en la sopa de letras cabe o no las palabras a encontrar.
     * En dado caso, generará la sopa de letras con solo las palabras que quepan dentro de la sopa de letras.
     */
    private boolean cabe(char[][] grid, String palabra, int r, int c, Direccion d) {
        int ultimo = palabra.length() - 1;
        int rFinal = r + d.getDr() * ultimo;
        int cFinal = c + d.getDc() * ultimo;
        if (rFinal < 0 || rFinal >= grid.length || cFinal < 0 || cFinal >= grid.length) {
            return false;
        }
        for (int i = 0; i <= ultimo; i++) {
            char actual = grid[r + i * d.getDr()][c + i * d.getDc()];
            if (actual != '\0' && actual != palabra.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Método que rellena la sopa de letras con valores aleatorios.
     */
    private void rellenar(char[][] grid) {
        for (char[] fila : grid) {
            for (int c = 0; c < fila.length; c++) {
                if (fila[c] == '\0') {
                    fila[c] = ABECEDARIO.charAt(random.nextInt(ABECEDARIO.length()));
                }
            }
        }
    }

    /*
     * Método que recolecta todas las clases anteriormente vistas y generara la matriz correspondiente.
     */
    private String[][] aMatrizDeTexto(char[][] grid) {
        String[][] resultado = new String[grid.length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid.length; c++) {
                resultado[r][c] = String.valueOf(grid[r][c]);
            }
        }
        return resultado;
    }

    /*
     * Método para guardar la hoja de texto de la sopa de letras.
     */
    public boolean guardar(String[][] celdas, String ruta) {
        String contenido = "";
        for (int r = 0; r < celdas.length; r++) {
            for (int c = 0; c < celdas[r].length; c++) {
                contenido = contenido + celdas[r][c];
            }
            if (r < celdas.length - 1) {
                contenido = contenido + "\n";
            }
        }
        try {
            Files.writeString(Path.of(ruta), contenido);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}