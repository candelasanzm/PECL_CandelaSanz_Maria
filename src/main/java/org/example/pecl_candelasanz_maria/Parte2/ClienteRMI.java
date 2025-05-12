package org.example.pecl_candelasanz_maria.Parte2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            ApocalipsisRMI apocalipsisRMI = (ApocalipsisRMI) registry.lookup("ApocalipsisService");


        }
    }
}
