package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javax.swing.*;

public class ApplicationController {
    @FXML
    private JTextField HumanosZonaComun;

    private Apocalipsis ap;

    @FXML
    public void initialize() {
        HumanosZonaComun = new JTextField();
        ap = new Apocalipsis(HumanosZonaComun);
        crearHumano();
    }

    @FXML
    protected void crearHumano() {
        Humano h = new Humano(ap);
        h.start();
    }
}
