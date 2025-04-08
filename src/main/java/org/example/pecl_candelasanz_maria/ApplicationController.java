package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ApplicationController {
    @FXML
    private TextField HumanosZonaComun;

    private Apocalipsis ap;

    @FXML
    public void initialize() {
        ap = new Apocalipsis(HumanosZonaComun);
        crearHumano();
    }

    @FXML
    protected void crearHumano() {
        Humano h = new Humano(ap);
        h.start();
    }
}
