package com.example.vista;

import java.io.File;

import com.example.modelo.Direccion;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class VistaGrafica {

    private static final int TAM_CELDA = 30;
    private static final String ESTILO_CELDA = "-fx-border-color: #cccccc; -fx-font-family: monospace; -fx-font-size: 16px; -fx-font-weight: bold;";
    private static final String ESTILO_NUMERO = "-fx-font-family: monospace; -fx-font-size: 12px; -fx-text-fill: #777777;";

    private final BorderPane raiz = new BorderPane();

    private final Button btnAbrir = new Button("Abrir archivo...");
    private final Button btnGenerar = new Button("Generar sopa");
    private final Button btnGuardar = new Button("Guardar sopa...");
    private final Spinner<Integer> spTamano = new Spinner<>(8, 20, 12);

    private final TextField campoPalabra = new TextField();
    private final Button btnBuscar = new Button("Buscar");
    private final Button btnResolver = new Button("Resolver");

    private final GridPane tablero = new GridPane();
    private final Label estado = new Label("Abre un archivo o genera una sopa para empezar.");

    private Label[][] etiquetas = new Label[0][0];

    public VistaGrafica() {
        campoPalabra.setPromptText("Palabra a buscar");
        campoPalabra.setPrefWidth(220);

        HBox fila1 = new HBox(8, btnAbrir, new Label("Tamaño:"), spTamano, btnGenerar, btnGuardar);
        HBox fila2 = new HBox(8, new Label("Palabra:"), campoPalabra, btnBuscar, btnResolver);
        fila1.setAlignment(Pos.CENTER_LEFT);
        fila2.setAlignment(Pos.CENTER_LEFT);
        spTamano.setPrefWidth(75);

        VBox arriba = new VBox(8, fila1, fila2);
        arriba.setPadding(new Insets(10));

        tablero.setAlignment(Pos.CENTER);
        StackPane centro = new StackPane(tablero);
        centro.setPadding(new Insets(10));
        ScrollPane scroll = new ScrollPane(centro);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        estado.setWrapText(true);
        estado.setPadding(new Insets(10));
        estado.setMinHeight(70);
        estado.setAlignment(Pos.TOP_LEFT);

        raiz.setTop(arriba);
        raiz.setCenter(scroll);
        raiz.setBottom(estado);

        setSopaCargada(false);
    }

    public Parent getRaiz() { return raiz; }

    public void alAbrir(Runnable accion) { 
        btnAbrir.setOnAction(e -> accion.run());
    }
    public void alGenerar(Runnable accion) {
        btnGenerar.setOnAction(e -> accion.run()); 
    }
    public void alGuardar(Runnable accion) { 
        btnGuardar.setOnAction(e -> accion.run()); 
    }
    public void alResolver(Runnable accion) { 
        btnResolver.setOnAction(e -> accion.run()); 
    }

    public void alBuscar(Runnable accion) {
        btnBuscar.setOnAction(e -> accion.run());
        campoPalabra.setOnAction(e -> accion.run());
    }

    public String getPalabra() { return campoPalabra.getText().trim(); }

    public int getTamano() { return spTamano.getValue(); }

    public void setSopaCargada(boolean cargada) {
        btnBuscar.setDisable(!cargada);
        btnResolver.setDisable(!cargada);
        btnGuardar.setDisable(!cargada);
        campoPalabra.setDisable(!cargada);
    }

    public void mostrarCuadricula(String[][] celdas) {
        tablero.getChildren().clear();
        int renglones = celdas.length;
        int columnas = celdas[0].length;
        etiquetas = new Label[renglones][columnas];

        for (int c = 0; c < columnas; c++) {
            tablero.add(crearEtiqueta(String.valueOf(c + 1), ESTILO_NUMERO), c + 1, 0);
        }
        for (int r = 0; r < renglones; r++) {
            tablero.add(crearEtiqueta(String.valueOf(r + 1), ESTILO_NUMERO), 0, r + 1);
            for (int c = 0; c < columnas; c++) {
                Label letra = crearEtiqueta(celdas[r][c], ESTILO_CELDA);
                etiquetas[r][c] = letra;
                tablero.add(letra, c + 1, r + 1);
            }
        }
    }

    private Label crearEtiqueta(String texto, String estilo) {
        Label l = new Label(texto);
        l.setMinSize(TAM_CELDA, TAM_CELDA);
        l.setPrefSize(TAM_CELDA, TAM_CELDA);
        l.setAlignment(Pos.CENTER);
        l.setStyle(estilo);
        return l;
    }

    public void limpiarResaltado() {
        for (Label[] fila : etiquetas) {
            for (Label l : fila) {
                l.setStyle(ESTILO_CELDA);
            }
        }
    }

    public void resaltar(int renglon, int columna, Direccion d, int longitud, String color) {
        for (int i = 0; i < longitud; i++) {
            int r = renglon - 1 + i * d.getDr();
            int c = columna - 1 + i * d.getDc();
            etiquetas[r][c].setStyle(ESTILO_CELDA + "-fx-background-color: " + color + ";");
        }
    }

    public void mostrarMensaje(String mensaje) {
        estado.setStyle("-fx-text-fill: black;");
        estado.setText(mensaje);
    }

    public void mostrarError(String mensaje) {
        estado.setStyle("-fx-text-fill: #c62828;");
        estado.setText(mensaje);
    }

    public File elegirArchivoParaAbrir() {
        return crearSelector("Abrir sopa de letras").showOpenDialog(raiz.getScene().getWindow());
    }

    public File elegirArchivoParaGuardar() {
        return crearSelector("Guardar sopa de letras").showSaveDialog(raiz.getScene().getWindow());
    }

    private FileChooser crearSelector(String titulo) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titulo);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        File actual = new File(System.getProperty("user.dir"));
        if (actual.isDirectory()) {
            fc.setInitialDirectory(actual);
        }
        return fc;
    }
}
