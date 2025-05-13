package org.example.pecl_candelasanz_maria;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ApocalipsisRMI extends Remote {
    int getHumanosRefugio() throws RemoteException;
    int getHumanosZonaComun() throws RemoteException;
    int getHumanosComedor() throws RemoteException;
    int getHumanosZonaDescanso() throws RemoteException;
    int[] getHumanosTuneles() throws RemoteException;
    int[] getHumanosZonaRiesgo() throws RemoteException;
    int[] getZombiesZonaRiesgo() throws RemoteException;
    String[] getZombiesLetales() throws RemoteException;
    void ejecucion() throws RemoteException;
    boolean estadoEjecucion() throws RemoteException;
}
