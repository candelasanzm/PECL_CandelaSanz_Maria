package org.example.pecl_candelasanz_maria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pecl_candelasanz_maria/servidorRMI.fxml"));
        Parent root = loader.load();

        ServidorRMIController app = loader.getController();

        stage.setTitle("Apocalipsis Zombie");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        try {
            Apocalipsis apocalipsis = new Apocalipsis(null, null, null);
            ImplementacionApocalipsisRMI servicio = new ImplementacionApocalipsisRMI(apocalipsis);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ApocalipsisService", servicio);

            System.out.println("Servidor Apocalipsis RMI Arrancado");
        } catch (IOException e){
            System.out.println("Error en el Servidor: " + e.getMessage());
        }
    }
}
