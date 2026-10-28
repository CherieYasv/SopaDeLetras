package com.example.controlador;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.example.modelo.GeneradorSopaLetras;
import com.example.modelo.Resultado;
import com.example.modelo.SopaDeLetras;
import com.example.vista.VistaGrafica;

/**
 * ControladorGrafico es la clase encargada de mandar a llamar todo lo necesario para
 * la correcta ejecución del interfaz gráfica. Teniendo agrupadas las variables más
 * importantes.
 * 
 * @author CAAA
 * @version 2
 */

public class ControladorGrafico {

    private String[] palabras;
    private static final String[] COLORES = {"#ffd54f", "#81d4fa", "#a5d6a7", "#ffb74d", "#ce93d8", "#80cbc4", "#90caf9"};

    private final VistaGrafica vista;
    private final GeneradorSopaLetras generador = new GeneradorSopaLetras();
    private SopaDeLetras sopa;

    /*
     * Método constructor encargado de dar pie a la interfaz.
     */
    public ControladorGrafico(VistaGrafica vista) {
        this.vista = vista;
        cargarPalabras("palabras.txt");
        vista.alAbrir(this::abrir);
        vista.alGenerar(this::generar);
        vista.alGuardar(this::guardar);
        vista.alBuscar(this::buscar);
        vista.alResolver(this::resolver);
    }

    /*
     * Método encargado de cargar las palabras correspendientes para buscar dentro de la sopa de letras.
     * Incluyendo en si mismo una base de palabras para la sopa de letras.
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
     * Método simple para abrir un archivo de sopas ya hecho.
     */
    private void abrir() {
        File archivo = vista.elegirArchivoParaAbrir();
        if (archivo == null) {
            return; 
        }
        SopaDeLetras nueva = new SopaDeLetras();
        if (nueva.cargarDesdeArchivo(archivo.getPath())) {
            usarSopa(nueva, "Sopa cargada de " + archivo.getName() + ".");
        } else {
            vista.mostrarError("No pude cargar \"" + archivo.getName()
                + "\". Revisa que tenga solo letras y que todos los renglones midan lo mismo.");
        }
    }

    /*
     * Método para generar las sopas de letras de diferentes tamaños.
     */
    private void generar() {
        String[][] celdas = generador.generar(vista.getTamano(), palabras);
        if (celdas == null) {
            vista.mostrarError("No pude generar la sopa. Prueba con un tamaño mayor.");
            return;
        }
        SopaDeLetras nueva = new SopaDeLetras();
        nueva.setCeldas(celdas);
        usarSopa(nueva, "Sopa nueva generada. Puedes buscar palabras o presionar Resolver.");
    }   

    /*
     * Método para guardar la sopa de letras actual en tus archivos locales.
     */
    private void guardar() {
        File archivo = vista.elegirArchivoParaGuardar();
        if (archivo == null) {
            return;
        }
        if (generador.guardar(sopa.getCeldas(), archivo.getPath())) {
            vista.mostrarMensaje("Sopa guardada en " + archivo.getName() + ".");
        } else {
            vista.mostrarError("No pude guardar el archivo.");
        }
    }

    /*
     * Método simple para evaluar y validar a la hora de escribir una palabra.
     */
    private void buscar() {
        String palabra = vista.getPalabra();
        if (palabra.isBlank()) {
            vista.mostrarError("Escribe una palabra.");
            return;
        }
        vista.limpiarResaltado();
        Resultado r = sopa.buscarPalabra(palabra);
        if (r.encontrada()) {
            vista.resaltar(r.renglon(), r.columna(), r.direccion(), longitud(palabra), COLORES[0]);
        }
        vista.mostrarMensaje(describir(palabra, r));
    }

    /*
     * Método para resolver en automatico la sopa de letras.
     */
    private void resolver() {
        vista.limpiarResaltado();
        String mensaje = "";
        for (int i = 0; i < palabras.length; i++) {
            Resultado r = sopa.buscarPalabra(palabras[i]);
            if (r.encontrada()) {
                vista.resaltar(r.renglon(), r.columna(), r.direccion(), longitud(palabras[i]), COLORES[i % COLORES.length]);
            }
            mensaje = mensaje + describir(palabras[i], r) + "\n";
        }
        vista.mostrarMensaje(mensaje.trim());
    }

    /*
     * Método para poder usar una sopa de letras ya establecida en el sistema local.
     */
    private void usarSopa(SopaDeLetras nueva, String mensaje) {
        sopa = nueva;
        vista.mostrarCuadricula(sopa.getCeldas());
        vista.setSopaCargada(true);
        vista.mostrarMensaje(mensaje);
    }

    /*
     * Método para evaluar, de manera simple, la longitud de la sopa de letras.
     */
    private int longitud(String palabra) {
        return palabra.replaceAll("\\s+", "").length();
    }

    /*
     * Método simple que imprime la dirección (escrita) de donde se encontro la palabra dentro de la sopa de letras.
     */
    private String describir(String palabra, Resultado r) {
        if (!r.encontrada()) {
            return "\"" + palabra + "\" NO está en la cuadrícula.";
        }
        return "\"" + palabra + "\" SÍ está. Empieza en renglón " + r.renglon()
                + ", columna " + r.columna() + " y va hacia " + r.direccion() + ".";
    }
}