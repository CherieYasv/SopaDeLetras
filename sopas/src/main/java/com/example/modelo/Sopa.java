package com.example.modelo;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Clase Sopa es la que generá las validaciones correspondientes en toda la sopa de letras.
 * 
 * @author CAAA
 * @version 1
 */

public class Sopa {

    private String[][] celdas;

    /*
     * Método que separa las palabras. Para su correcta lectura.
     */
    String[] separar(String linea) {
        return linea.trim().split("\\s+");
    }

    /*
     * Método para validar elementos.
     */
    boolean esElementoValido(String elemento) {
        return elemento != null && !elemento.isBlank();
    }

    /*
     * Método encargado de cargar el archivo de la sopa de letras a partir de su ruta.
     */
    public boolean cargarDesdeArchivo(String ruta) {
        List<String> lineas;
        try {
            lineas = Files.readAllLines(Path.of(ruta)).stream()
                    .map(String::trim)
                    .filter(l -> !l.isEmpty())
                    .toList();
        } catch (Exception e) {
            return false;
        }

        if (lineas.isEmpty()) {
            return false;
        }

        String[][] nueva = new String[lineas.size()][];
        for (int i = 0; i < nueva.length; i++) {
            nueva[i] = separar(lineas.get(i));
        }
        if (!validar(nueva)) {
            return false;
        }
        celdas = nueva;
        return true;
    }

    //Settes y Gettes

    public boolean setCeldas(String[][] nueva) {
        if (!validar(nueva)) {
            return false;
        }
        celdas = copiar(nueva);
        return true;
    }

    /*
     * Método para validar las variables tipo matriz.
     */
    private boolean validar(String[][] matriz) {
        if (matriz == null || matriz.length == 0 || matriz[0].length == 0) {
            return false;
        }
        int columnas = matriz[0].length;
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i].length != columnas) {
                return false;
            }
            if (!Arrays.stream(matriz[i]).allMatch(this::esElementoValido)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Método para validar que no te estas saliendo de los valores de la matriz.
     */
    public boolean hayCuadricula() {
        return celdas != null;
    }

    //Settes y Gettes.
    public int getRenglones() {
        return celdas.length;
    }

    public int getColumnas() {
        return Array.getLength(celdas[0]);
    }

    public String[][] getCeldas() {
        return copiar(celdas);
    }

    /*
     * Método para copiar las matrices para sus diferentes usos (como guardarlo)
     */
    private String[][] copiar(String[][] original) {
        String[][] copia = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copia[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copia;
    }

    /*
     * Método para buscar la palabra, usando el método para separarlo y asi convertirlo en un String.
     */
    public Resultado buscarPalabra(String palabra) {
        return buscar(separar(palabra));
    }

    /*
     * Método para buscar dentro de la matriz.
     */
    public Resultado buscar(String[] secuencia) {
        if (celdas == null || secuencia.length == 0) {
            return Resultado.NO_ENCONTRADA;
        }
        for (int r = 0; r < getRenglones(); r++) {
            for (int c = 0; c < getColumnas(); c++) {
                for (Direccion d : Direccion.values()) {
                    if (coincide(r, c, d, secuencia)) {
                        return new Resultado(true, r + 1, c + 1, d);
                    }
                }
            }
        }
        return Resultado.NO_ENCONTRADA;
    }

    /*
     * Método que valida si realmente se encontro la palabra.
     */
    private boolean coincide(int r, int c, Direccion d, String[] secuencia) {
        int ultimo = secuencia.length - 1;
        int rFinal = r + d.getDr() * ultimo;
        int cFinal = c + d.getDc() * ultimo;
        if (rFinal < 0 || rFinal >= getRenglones() || cFinal < 0 || cFinal >= getColumnas()) {
            return false;
        }

        String[] tramo = new String[secuencia.length];
        for (int i = 0; i < tramo.length; i++) {
            tramo[i] = celdas[r + i * d.getDr()][c + i * d.getDc()];
        }
        return Arrays.equals(tramo, secuencia);
    }
}