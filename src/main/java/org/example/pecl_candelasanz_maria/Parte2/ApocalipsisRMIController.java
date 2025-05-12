package org.example.pecl_candelasanz_maria.Parte2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApocalipsisRMIController {
    @FXML
    private TextField txtHumanosZonaComun;

    @FXML
    private TextField txtHumanosTuneles;

    @FXML
    private TextField txtHumanosZonasRiesgo;

    @FXML
    private TextField txtZombiesZonasRiesgo;

    @FXML
    private ListView<String> rankingZombies;

    @FXML
    private Button btnEjecucion;

    private ApocalipsisRMI apocalipsis;

    @FXML
    public void initialize() {
        conectarRMI();
        inicializarActualizacion();

        btnEjecucion.setOnAction(e -> {
            try {
                apocalipsis.ejecucion();
            } catch (Exception ex) {
                System.out.println("Error al cambiar de estado de ejecución: " + ex.getMessage());
            }
        });
    }

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

    private void inicializarActualizacion(){
        new Thread(() -> {
            try {
                while (true){
                    Platform.runLater(this::actualizarInterfaz);
                }
            } catch (Exception e){
                System.out.println("Error al actualizar RMI: " + e.getMessage());
            }
        }).start();
    }

    private void actualizarInterfaz(){
        try {
            txtHumanosZonaComun.setText(String.valueOf(apocalipsis.getHumanosRefugio()));
            txtHumanosTuneles.setText(arrayToString(apocalipsis.getHumanosTuneles()));
            txtHumanosZonasRiesgo.setText(arrayToString(apocalipsis.getHumanosZonaRiesgo()));
            txtZombiesZonasRiesgo.setText(arrayToString(apocalipsis.getZombiesZonaRiesgo()));
            rankingZombies.getItems().setAll(apocalipsis.getZombiesLetales());
        } catch (Exception e) {
            System.out.println("Error al actualizar interfaz: " + e.getMessage());
        }
    }

    private String arrayToString(int[] array){
        String resultado = "";
        for (int i = 0; i < array.length; i++){
            resultado += array[i] + " ";
        }
        return resultado.trim(); // Elimina los espacios extra
    }
}
