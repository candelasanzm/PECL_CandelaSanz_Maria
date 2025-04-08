package org.example.pecl_candelasanz_maria;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.swing.*;

public class HelloController {
    @FXML
    private JTextField zonaComunTextField;

    private Apocalipsis ap;

    @FXML
    public void initialize() {
        ap = new Apocalipsis(zonaComunTextField);
    }

    @FXML
    protected void crearHumano() {
        Humano h = new Humano(ap);
        h.start();
    }
}
