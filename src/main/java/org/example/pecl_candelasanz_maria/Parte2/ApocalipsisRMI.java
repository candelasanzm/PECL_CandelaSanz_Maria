package org.example.pecl_candelasanz_maria.Parte2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ApocalipsisRMI extends Remote {
    int getHumanosRefugio() throws RemoteException;
    int[] getHumanosTuneles() throws RemoteException;
    int[] getHumanosZonaRiesgo() throws RemoteException;
    int[] getZombiesZonaRiesgo() throws RemoteException;
    String[] getZombiesLetales() throws RemoteException;
    void ejecucion() throws RemoteException;
}
