package org.example.pecl_candelasanz_maria.Parte2;

import org.example.pecl_candelasanz_maria.Parte1.Apocalipsis;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorRMI {
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
