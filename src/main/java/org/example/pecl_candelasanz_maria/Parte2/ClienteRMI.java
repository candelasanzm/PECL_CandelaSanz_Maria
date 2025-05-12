package org.example.pecl_candelasanz_maria.Parte2;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ApocalipsisRMI apocalipsisRMI = (ApocalipsisRMI) registry.lookup("ApocalipsisService");


        } catch (IOException e){
            System.out.println("Error en el cliente: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
