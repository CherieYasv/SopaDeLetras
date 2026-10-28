package com.example.modelo;

/**
 * Clase Resultado es la clase base que evalua la salida de la palabra ingresada.
 * 
 * @author CAAA
 * @version 1
 */

public class Resultado {

    public static final Resultado NO_ENCONTRADA = new Resultado(false, 0, 0, null);

    private final boolean encontrada;
    private final int renglon;
    private final int columna;
    private final Direccion direccion;

    /*
     * Contructor a rellenar de la clase.
     */
    public Resultado(boolean encontrada, int renglon, int columna, Direccion direccion) {
        this.encontrada = encontrada;
        this.renglon = renglon;
        this.columna = columna;
        this.direccion = direccion;
    }

    public boolean encontrada() {
        return encontrada;
    }

    public int renglon() {
        return renglon;
    }

    public int columna() {
        return columna;
    }

    public Direccion direccion() {
        return direccion;
    }
}