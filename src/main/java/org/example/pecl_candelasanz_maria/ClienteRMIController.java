package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMIController {
    @FXML
    private TextField txtHumanosZonaComun;

    @FXML
    private TextField txtHumanosTunel1;

    @FXML
    private TextField txtHumanosTunel2;

    @FXML
    private TextField txtHumanosTunel3;

    @FXML
    private TextField txtHumanosTunel4;

    @FXML
    private TextField txtHumanosZonaRiesgo1;

    @FXML
    private TextField txtHumanosZonaRiesgo2;

    @FXML
    private TextField txtHumanosZonaRiesgo3;

    @FXML
    private TextField txtHumanosZonaRiesgo4;

    @FXML
    private TextField txtZombiesZonaRiesgo1;

    @FXML
    private TextField txtZombiesZonaRiesgo2;

    @FXML
    private TextField txtZombiesZonaRiesgo3;

    @FXML
    private TextField txtZombiesZonaRiesgo4;

    @FXML
    private ListView<String> rankingZombies;

    @FXML
    private Button btnEjecucion;

    private ApocalipsisRMI apocalipsis;
    private Thread hilo;

    @FXML
    public void initialize() {
        conectarRMI();
        inicializarActualizacion();
    }

    @FXML
    private void ejecucion(){
        try {
            apocalipsis.ejecucion();
            actualizarBoton();
        } catch (Exception ex) {
            System.out.println("Error al cambiar de estado de ejecución: " + ex.getMessage());
        }
    }

    // Establece la conexión con el servicio RMI obteniendo la referencia al servidor remoto
    private void conectarRMI(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            apocalipsis = (ApocalipsisRMI) registry.lookup("ApocalipsisService");
        } catch (IOException e){
            System.out.println("Error al conectar RMI: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error al conectar RMI: " + e.getMessage());
        }
    }

    // Crea un hilo que se ejecuta en un bucle continuo y va actualizando la interfaz
    private void inicializarActualizacion(){
        hilo = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()){
                    Platform.runLater(this::actualizarInterfaz);
                }
            } catch (Exception e){
                System.out.println("Error al actualizar RMI: " + e.getMessage());
            }
        });
        hilo.start();
    }

    // Actualiza los valores que tienen que aparecer en la interfaz
    private void actualizarInterfaz(){
        try {
            txtHumanosZonaComun.setText(String.valueOf(apocalipsis.getHumanosRefugio()));

            int[] tuneles = apocalipsis.getHumanosTuneles();
            txtHumanosTunel1.setText(String.valueOf(tuneles[0]));
            txtHumanosTunel2.setText(String.valueOf(tuneles[1]));
            txtHumanosTunel3.setText(String.valueOf(tuneles[2]));
            txtHumanosTunel4.setText(String.valueOf(tuneles[3]));

            int[] humanosZonasRiesgo = apocalipsis.getHumanosZonaRiesgo();
            txtHumanosZonaRiesgo1.setText(String.valueOf(humanosZonasRiesgo[0]));
            txtHumanosZonaRiesgo2.setText(String.valueOf(humanosZonasRiesgo[1]));
            txtHumanosZonaRiesgo3.setText(String.valueOf(humanosZonasRiesgo[2]));
            txtHumanosZonaRiesgo4.setText(String.valueOf(humanosZonasRiesgo[3]));

            int[] zombiesZonaRiesgo = apocalipsis.getZombiesZonaRiesgo();
            txtZombiesZonaRiesgo1.setText(String.valueOf(zombiesZonaRiesgo[0]));
            txtZombiesZonaRiesgo2.setText(String.valueOf(zombiesZonaRiesgo[1]));
            txtZombiesZonaRiesgo3.setText(String.valueOf(zombiesZonaRiesgo[2]));
            txtZombiesZonaRiesgo4.setText(String.valueOf(zombiesZonaRiesgo[3]));

            rankingZombies.getItems().setAll(apocalipsis.getZombiesLetales());
        } catch (Exception e) {
            System.out.println("Error al actualizar interfaz: " + e.getMessage());
        }
    }

    // Cambia el texto del botón en función de si la ejecución está en curso o detenida
    private void actualizarBoton() {
        try {
            boolean enEjecucion = apocalipsis.estadoEjecucion();
            btnEjecucion.setText(enEjecucion ? "Detener Ejecución" : "Reanudar Ejecución");
        } catch (Exception e) {
            System.out.println("Error al actualizar Boton: " + e.getMessage());
        }
    }
}
