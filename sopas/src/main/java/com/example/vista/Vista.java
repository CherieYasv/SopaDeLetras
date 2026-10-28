package com.example.vista;

import com.example.modelo.Resultado;

public interface Vista {
    int mostrarMenu();
    String pedirPalabra();
    String pedirArchivo(String mensaje);
    int pedirTamano();
    void mostrarCuadricula(String[][] celdas);
    void mostrarResultado(String palabra, Resultado resultado);
    void mostrarMensaje(String mensaje);
    void mostrarError(String mensaje);
}
