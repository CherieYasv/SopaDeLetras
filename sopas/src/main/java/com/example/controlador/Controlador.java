package com.example.controlador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.example.modelo.GeneradorSopaLetras;
import com.example.modelo.SopaDeLetras;
import com.example.vista.Vista;

/**
 * Controlador es la clase que se encarga de maniobrar las principales variables y
 * métodos que se encargan de mostrar en TERMINAL el programa.
 * 
 * @author CAAA
 * @version 2
 */

public class Controlador {

    private String[] palabras;

    private final Vista vista;
    private final GeneradorSopaLetras generador = new GeneradorSopaLetras();

    /*
     * Constructor para rellenar de la clase Controlador.
     */
    public Controlador(Vista vista) {
        this.vista = vista;
        cargarPalabras("palabras.txt");
    }

    /*
     * Método encargado de citar la "base" de la sopa de letras.
     */
    private void cargarPalabras(String ruta) {
        try {
            palabras = Files.readAllLines(Path.of(ruta)).stream()
                    .map(String::trim)
                    .filter(p -> !p.isEmpty())
                    .toArray(String[]::new);
        } catch (Exception e) {
            vista.mostrarError("No se encontró el archivo. Se usarán valores por defecto.");
            palabras = new String[]{"Gregorio", "Coronel", "Macondo"};
        }
    }

    /*
     * Método encargado de iniciar y dar las opciones del menu en la sopa de letras de terminal.
     */
    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenu();
            switch (opcion) {
                case 1 -> buscarPalabra();
                case 2 -> generarSopa();
                case 3 -> resolver();
                case 0 -> vista.mostrarMensaje("Hasta luego!");
                default -> vista.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /* 
     * Método encargado de leer la palabra a ingresar. Llamando el método para evaluar.
     */
    private void buscarPalabra() {
        String palabra = vista.pedirPalabra();
        if (palabra.isBlank()) {
            vista.mostrarError("Escribe una palabra.");
            return;
        }
        SopaDeLetras sopa = cargarSopa();
        if (sopa == null) {
            return;
        }
        vista.mostrarCuadricula(sopa.getCeldas());
        vista.mostrarResultado(palabra, sopa.buscarPalabra(palabra));
    }

    /*
     * Método que carga la sopa de letras en cuestión y lo resuelve en automatico.
     */
    private void resolver() {
        SopaDeLetras sopa = cargarSopa();
        if (sopa == null) {
            return;
        }
        vista.mostrarCuadricula(sopa.getCeldas());
        Arrays.stream(palabras)
            .forEach(p -> vista.mostrarResultado(p, sopa.buscarPalabra(p)));
    }

    /*
     * Método que llama los diversos métodos encargados de generar la sopa de letras.
     * Generando una sopa de letras aleatoria. 
     */
    private void generarSopa() {
        int tamano = vista.pedirTamano();
        String ruta = vista.pedirArchivo("Nombre del archivo donde guardarla (ej. sopa1.txt): ");
        if (ruta.isBlank()) {
            vista.mostrarError("Escribe un nombre de archivo.");
            return;
        }
        String[][] celdas = generador.generar(tamano, palabras);
        if (celdas == null) {
            vista.mostrarError("No pude generar la sopa. Revisa que el tamaño sea mínimo 8.");
            return;
        }
        if (!generador.guardar(celdas, ruta)) {
            vista.mostrarError("No pude guardar el archivo. Revisa que el nombre sea válido.");
            return;
        }
        vista.mostrarMensaje("Sopa guardada en " + ruta + ".");
        vista.mostrarCuadricula(celdas);
    }

    /*
     * Método encargado de navegar en tus archivos y con un nombre, cargar una sopa de letras guardada.
     */
    private SopaDeLetras cargarSopa() {
        String ruta = vista.pedirArchivo("Nombre del archivo de la sopa: ");
        SopaDeLetras sopa = new SopaDeLetras();
        if (sopa.cargarDesdeArchivo(ruta)) {
            return sopa;
        }
        vista.mostrarError("No pude cargar la sopa. Revisa el nombre del archivo y que tenga el formato correcto.");
        return null;
    }
}