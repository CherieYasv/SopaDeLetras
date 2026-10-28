package com.example;

import com.example.controlador.ControladorGrafico;
import com.example.vista.VistaGrafica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppSopa extends Application {

    @Override
    public void start(Stage stage) {
        VistaGrafica vista = new VistaGrafica();
        new ControladorGrafico(vista);

        stage.setTitle("Sopa de letras");
        stage.setScene(new Scene(vista.getRaiz(), 720, 640));
        stage.show();
    }
}
