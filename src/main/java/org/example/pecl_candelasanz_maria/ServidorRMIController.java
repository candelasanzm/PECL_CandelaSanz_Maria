package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServidorRMIController {
    @FXML
    private TextField HumanosZonaComun;
    @FXML
    private TextField EntradaTunel1;
    @FXML
    private TextField EntradaTunel2;
    @FXML
    private TextField EntradaTunel3;
    @FXML
    private TextField EntradaTunel4;
    @FXML
    private TextField Tunel1;
    @FXML
    private TextField Tunel2;
    @FXML
    private TextField Tunel3;
    @FXML
    private TextField Tunel4;
    @FXML
    private TextField SalidaTunel1;
    @FXML
    private TextField SalidaTunel2;
    @FXML
    private TextField SalidaTunel3;
    @FXML
    private TextField SalidaTunel4;
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

    private Apocalipsis apocalipsis;
    private ApocalipsisLogs apocalipsisLogs = ApocalipsisLogs.getInstancia();

    @FXML
    public void initialize() {
        // Creo listas de TextFields para el constructor
        TextField[] zonasTxtField = {
                HumanosZonaComun, HumanosZonaDescanso, HumanosComedor,
                EntradaTunel1, EntradaTunel2, EntradaTunel3, EntradaTunel4,
                Tunel1, Tunel2, Tunel3, Tunel4,
                SalidaTunel1, SalidaTunel2, SalidaTunel3, SalidaTunel4,
                HumanosRiesgo1, HumanosRiesgo2, HumanosRiesgo3, HumanosRiesgo4
        };

        TextField[] zombiesTxtField = {
                ZombiesRiesgo1, ZombiesRiesgo2, ZombiesRiesgo3, ZombiesRiesgo4
        };

        //Crear apocalipsis
        apocalipsis = new Apocalipsis(zonasTxtField, HumanosComida, zombiesTxtField);

        //Crear primer zombie
        crearZombie();

        //Crear humanos
        new Thread(() -> crearHumano()).start();
    }

    @FXML
    protected void crearHumano() {
        for(int i = 0; i < 30; i++) {
            try{
                Humano h = new Humano(apocalipsis, apocalipsis.getZonas(0));
                h.start();
                Thread.sleep((int)(Math.random() * 1500) + 500); // Se crean escalonados
            }catch(Exception e){
                apocalipsisLogs.registrarEvento("Error al crear los humanos " + e);
            }

        }
    }

    @FXML
    protected void crearZombie() { // Paciente 0
        try {
            Zombie z = new Zombie(apocalipsis,"Z0000");
            z.start();
        } catch (Exception e) {
            apocalipsisLogs.registrarEvento("Error al crear zombi: " + e.getMessage());
        }
    }
}
