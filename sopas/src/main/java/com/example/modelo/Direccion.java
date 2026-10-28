package com.example.modelo;

import java.util.Arrays;

/**
 * Dirección es una clase encargada de imprimir correctamente las direcciones y
 * coordenadas de las palabras encontradas dentro de la sopa de letras.
 * 
 * @author CAAA
 * @version 1
 */
public class Direccion {

    public static final Direccion DERECHA = new Direccion("derecha", 0, 1);
    public static final Direccion ABAJO = new Direccion("abajo", 1, 0);
    public static final Direccion IZQUIERDA = new Direccion("izquierda", 0, -1);
    public static final Direccion ARRIBA = new Direccion("arriba", -1, 0);
    public static final Direccion DIAGONAL_ABAJO_DERECHA = new Direccion("diagonal abajo-derecha", 1, 1);
    public static final Direccion DIAGONAL_ARRIBA_DERECHA = new Direccion("diagonal arriba-derecha", -1, 1);
    public static final Direccion DIAGONAL_ABAJO_IZQUIERDA = new Direccion("diagonal abajo-izquierda", 1, -1);
    public static final Direccion DIAGONAL_ARRIBA_IZQUIERDA = new Direccion("diagonal arriba-izquierda", -1, -1);

    /*
     * Constructor de la clase.
     */
    private static final Direccion[] TODAS = {
        DERECHA,
        ABAJO,
        IZQUIERDA,
        ARRIBA,
        DIAGONAL_ABAJO_DERECHA,
        DIAGONAL_ARRIBA_DERECHA,
        DIAGONAL_ABAJO_IZQUIERDA,
        DIAGONAL_ARRIBA_IZQUIERDA
    };

    private final String descripcion;
    private final int dr;
    private final int dc;

    /*
     * Constructor ha rellenar de la clase.
     */
    private Direccion(String descripcion, int dr, int dc) {
        this.descripcion = descripcion;
        this.dr = dr;
        this.dc = dc;
    }

    /* 
     * Método para evaluar valores.
     */
    public static Direccion[] values() {
        return Arrays.copyOf(TODAS, TODAS.length);
    }

    /*
     * get de la variable dr.
     */
    public int getDr() {
        return dr;
    }

    /*
     * get de la variable dc.
     */
    public int getDc() {
        return dc;
    }

    /*
     * toString del archivo.
     */
    @Override
    public String toString() {
        return descripcion;
    }
}