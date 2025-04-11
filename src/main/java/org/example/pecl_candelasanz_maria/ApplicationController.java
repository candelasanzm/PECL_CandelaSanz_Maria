package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.swing.*;

public class ApplicationController {
    @FXML
    private TextField HumanosZonaComun;
    @FXML
    private TextField EntradaT1;
    @FXML
    private TextField EntradaT2;
    @FXML
    private TextField EntradaT3;
    @FXML
    private TextField EntradaT4;
    @FXML
    private TextField Tunel1;
    @FXML
    private TextField Tunel2;
    @FXML
    private TextField Tunel3;
    @FXML
    private TextField Tunel4;
    @FXML
    private TextField HumanosRiesgo1;
    @FXML
    private TextField HumanosRiesgo2;
    @FXML
    private TextField HumanosRiesgo3;
    @FXML
    private TextField HumanosRiesgo4;
    @FXML
    private TextField SalidaT1;
    @FXML
    private TextField SalidaT2;
    @FXML
    private TextField SalidaT3;
    @FXML
    private TextField SalidaT4;
    @FXML
    private TextField HumanosZonaDescanso;
    @FXML
    private TextField HumanosComedor;
    @FXML
    private TextField HumanosComida;

    private Apocalipsis ap;

    @FXML
    public void initialize() {
        TextField[] EntradaT = {EntradaT1, EntradaT2, EntradaT3, EntradaT4};
        TextField[] InteriorTunel = {Tunel1, Tunel2, Tunel3, Tunel4};
        TextField[] HumanosRiesgo = {HumanosRiesgo1, HumanosRiesgo2, HumanosRiesgo3, HumanosRiesgo4};
        TextField[] SalidaT = {SalidaT1, SalidaT2, SalidaT3, SalidaT4};

        ap = new Apocalipsis(HumanosZonaComun, EntradaT, InteriorTunel, HumanosRiesgo, SalidaT, HumanosZonaDescanso, HumanosComedor, HumanosComida);
        crearHumano();
    }

    @FXML
    protected void crearHumano() {
        for(int i = 0; i < 10; i++) {
            try{
                Humano h = new Humano(ap);
                h.start();
                //h.sleep((int)(Math.random() * 1500) + 500); //se crean escalonados
            }catch(Exception e){
                System.out.println("Error al crear los humanos " + e);
            }

        }
    }
}
