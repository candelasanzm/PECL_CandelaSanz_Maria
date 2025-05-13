package org.example.pecl_candelasanz_maria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;

public class ClienteRMI extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pecl_candelasanz_maria/clienteRMI.fxml"));
        Parent root = loader.load();

        ClienteRMIController app = loader.getController();

        stage.setTitle("Apocalipsis Zombie");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        try {
            ApocalipsisRMI apocalipsisRMI = (ApocalipsisRMI) Naming.lookup("ApocalipsisService");


        } catch (IOException e){
            System.out.println("Error en el cliente: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
