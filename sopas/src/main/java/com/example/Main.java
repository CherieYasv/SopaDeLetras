package com.example;
import com.example.controlador.Controlador;
import com.example.vista.VistaConsola;

/**
 * Clase Main es la que ejecuta el programa en la terminal.
 */
public class Main {
    public static void main(String[] args) {
        new Controlador(new VistaConsola()).iniciar();
    }
}
