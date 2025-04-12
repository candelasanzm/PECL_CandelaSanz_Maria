package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ApplicationController {
    @FXML
    private TextField HumanosZonaComun;
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
    private TextField HumanosZonaDescanso;
    @FXML
    private TextField HumanosComedor;
    @FXML
    private TextField HumanosComida;
    @FXML
    private TextField ZombiesRiesgo1;
    @FXML
    private TextField ZombiesRiesgo2;
    @FXML
    private TextField ZombiesRiesgo3;
    @FXML
    private TextField ZombiesRiesgo4;

    private Apocalipsis ap;

    @FXML
    public void initialize() {
        // Creo arrays de TextFields para el constructor
        TextField[] zonasTxtField = {
                HumanosZonaComun, Tunel1, Tunel2, Tunel3, Tunel4, HumanosRiesgo1, HumanosRiesgo2, HumanosRiesgo3, HumanosRiesgo4, HumanosZonaDescanso, HumanosComedor
        };
        TextField[] zombiesTxtField = {
                ZombiesRiesgo1, ZombiesRiesgo2, ZombiesRiesgo3, ZombiesRiesgo4
        };

        /*TextField[] EntradaT = {EntradaT1, EntradaT2, EntradaT3, EntradaT4};
        TextField[] InteriorTunel = {Tunel1, Tunel2, Tunel3, Tunel4};
        TextField[] HumanosRiesgo = {HumanosRiesgo1, HumanosRiesgo2, HumanosRiesgo3, HumanosRiesgo4};
        TextField[] SalidaT = {SalidaT1, SalidaT2, SalidaT3, SalidaT4};
        TextField[] ZombiesRiesgo = {ZombiesRiesgo1, ZombiesRiesgo2, ZombiesRiesgo3, ZombiesRiesgo4};*/

        ap = new Apocalipsis(zonasTxtField, HumanosComida, zombiesTxtField);
        //Humano humano = (Humano) new Thread();
        crearHumano();
        crearZombie();
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

    @FXML
    protected void crearZombie() { //Paciente 0
        Zombie z = new Zombie(ap);
        z.start();
    }
}
