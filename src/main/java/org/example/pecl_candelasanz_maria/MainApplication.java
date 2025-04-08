package org.example.pecl_candelasanz_maria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista.fxml"));
        Parent root = loader.load();

        stage.setTitle("Apocalipsis Zombie");
        stage.setScene(new Scene(root));
        stage.show();
        ApplicationController app = new ApplicationController();
        app.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
