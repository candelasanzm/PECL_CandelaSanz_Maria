package org.example.pecl_candelasanz_maria.Parte2;

import org.example.pecl_candelasanz_maria.Parte1.Apocalipsis;
import org.example.pecl_candelasanz_maria.Parte1.ListaHilosZombie;
import org.example.pecl_candelasanz_maria.Parte1.Zombie;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ImplementacionApocalipsisRMI extends UnicastRemoteObject implements ApocalipsisRMI {
    private Apocalipsis apocalipsis;
    private boolean enEjecucion = true;

    public ImplementacionApocalipsisRMI(Apocalipsis apocalipsis) throws RemoteException {
        this.apocalipsis = apocalipsis;
    }

    @Override
    public int getHumanosRefugio() throws RemoteException {
        // Zona Común + Zona de Descanso + Comedor
        return apocalipsis.getListaHumanosEnZona(0).getListado().size() + apocalipsis.getListaHumanosEnZona(1).getListado().size() + apocalipsis.getListaHumanosEnZona(2).getListado().size();
    }

    @Override
    public int[] getHumanosTuneles() throws RemoteException {
        return new int[] {
                apocalipsis.getListaHumanosEnZona(7).getListado().size(),
                apocalipsis.getListaHumanosEnZona(8).getListado().size(),
                apocalipsis.getListaHumanosEnZona(9).getListado().size(),
                apocalipsis.getListaHumanosEnZona(10).getListado().size(),
        };
    }

    @Override
    public int[] getHumanosZonaRiesgo() throws RemoteException {
        return new int[] {
                apocalipsis.getListaHumanosEnZona(15).getListado().size(),
                apocalipsis.getListaHumanosEnZona(16).getListado().size(),
                apocalipsis.getListaHumanosEnZona(17).getListado().size(),
                apocalipsis.getListaHumanosEnZona(18).getListado().size(),
        };
    }

    @Override
    public int[] getZombiesZonaRiesgo() throws RemoteException {
        return new int[] {
                apocalipsis.getListaZombies()[0].getListado().size(),
                apocalipsis.getListaZombies()[1].getListado().size(),
                apocalipsis.getListaZombies()[2].getListado().size(),
                apocalipsis.getListaZombies()[3].getListado().size(),
        };
    }

    @Override
    public String[] getZombiesLetales() throws RemoteException {
        // Obtengo las listas de Zombies de cada Zona de Riesgo
        ListaHilosZombie[] listasZombies = apocalipsis.getListaZombies();

        // Uno todas las listas en una única lista
        ArrayList<Zombie> todosZombies = new ArrayList<>();
        for (int i = 0; i < listasZombies.length; i++) {
            todosZombies.addAll(listasZombies[i].getListado());
        }

        // Ordenar por número de muertes
        todosZombies.sort((z1, z2) -> z2.getContadorMuertes() - z1.getContadorMuertes());

        // Creo el ranking con los 3 zombies más letales
        int cantidad = Math.min(3, todosZombies.size());
        String[] ranking = new String[cantidad];

        for (int i = 0; i < cantidad; i++) {
            Zombie zombie = todosZombies.get(i);
            ranking[i] = zombie.getID() + " - " + zombie.getContadorMuertes() + " muertes";
        }

        return ranking;
    }

    @Override
    public synchronized void ejecucion() throws RemoteException {
        enEjecucion = !enEjecucion;

        if (enEjecucion){
            apocalipsis.getPaso().abrir();
            System.out.println("Simulación Reanudada");
        } else {
            apocalipsis.getPaso().cerrar();
            System.out.println("Simulación Detenida");
        }
    }

    @Override
    public boolean estadoEjecucion() throws RemoteException {
        return enEjecucion;
    }
}
