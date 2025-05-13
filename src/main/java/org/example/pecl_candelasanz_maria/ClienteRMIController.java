package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.rmi.Naming;

public class ClienteRMIController {

    @FXML
    private TextField txtHumanosRefugio;

    @FXML
    private TextField txtHumanosZonaComun;

    @FXML
    private TextField txtHumanosComedor;

    @FXML
    private TextField txtHumanosZonaDescanso;

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

    private ApocalipsisRMI apocalipsisRMI;
    private Thread hilo;

    @FXML
    public void initialize() {
        try {
            apocalipsisRMI = (ApocalipsisRMI) Naming.lookup("ApocalipsisService");
            inicializarActualizacion();

            btnEjecucion.setOnAction(event -> ejecucion());
        } catch (IOException e){
            System.out.println("Error al conectar RMI: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error al conectar RMI: " + e.getMessage());
        }
    }

    @FXML
    private void ejecucion(){
        try {
            apocalipsisRMI.ejecucion();
            actualizarBoton();
        } catch (Exception ex) {
            System.out.println("Error al cambiar de estado de ejecución: " + ex.getMessage());
        }
    }

    // Crea un hilo que se ejecuta en un bucle continuo y va actualizando la interfaz
    private void inicializarActualizacion(){
        hilo = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()){
                    Platform.runLater(this::actualizarInterfaz);
                    Thread.sleep(3000);
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
            txtHumanosRefugio.setText(String.valueOf(apocalipsisRMI.getHumanosRefugio()));
            txtHumanosZonaComun.setText(String.valueOf(apocalipsisRMI.getHumanosZonaComun()));
            txtHumanosComedor.setText(String.valueOf(apocalipsisRMI.getHumanosComedor()));
            txtHumanosZonaDescanso.setText(String.valueOf(apocalipsisRMI.getHumanosZonaDescanso()));

            int[] tuneles = apocalipsisRMI.getHumanosTuneles();
            txtHumanosTunel1.setText(String.valueOf(tuneles[0]));
            txtHumanosTunel2.setText(String.valueOf(tuneles[1]));
            txtHumanosTunel3.setText(String.valueOf(tuneles[2]));
            txtHumanosTunel4.setText(String.valueOf(tuneles[3]));

            int[] humanosZonasRiesgo = apocalipsisRMI.getHumanosZonaRiesgo();
            txtHumanosZonaRiesgo1.setText(String.valueOf(humanosZonasRiesgo[0]));
            txtHumanosZonaRiesgo2.setText(String.valueOf(humanosZonasRiesgo[1]));
            txtHumanosZonaRiesgo3.setText(String.valueOf(humanosZonasRiesgo[2]));
            txtHumanosZonaRiesgo4.setText(String.valueOf(humanosZonasRiesgo[3]));

            int[] zombiesZonaRiesgo = apocalipsisRMI.getZombiesZonaRiesgo();
            txtZombiesZonaRiesgo1.setText(String.valueOf(zombiesZonaRiesgo[0]));
            txtZombiesZonaRiesgo2.setText(String.valueOf(zombiesZonaRiesgo[1]));
            txtZombiesZonaRiesgo3.setText(String.valueOf(zombiesZonaRiesgo[2]));
            txtZombiesZonaRiesgo4.setText(String.valueOf(zombiesZonaRiesgo[3]));

            rankingZombies.getItems().setAll(apocalipsisRMI.getZombiesLetales());
        } catch (Exception e) {
            System.out.println("Error al actualizar interfaz: " + e.getMessage());
        }
    }

    // Cambia el texto del botón en función de si la ejecución está en curso o detenida
    private void actualizarBoton() {
        try {
            boolean enEjecucion = apocalipsisRMI.estadoEjecucion();
            btnEjecucion.setText(enEjecucion ? "Detener Ejecución" : "Reanudar Ejecución");
        } catch (Exception e) {
            System.out.println("Error al actualizar Boton: " + e.getMessage());
        }
    }
}
