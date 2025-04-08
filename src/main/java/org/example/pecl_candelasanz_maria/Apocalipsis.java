package org.example.pecl_candelasanz_maria;

import javax.swing.*;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    private ListaHilos listaDentroZonaComun;
    private JTextField txtDentroZonaComun;
    private Semaphore semaforo;

    public Apocalipsis(JTextField txtDentroZonaComun){
        this.txtDentroZonaComun = txtDentroZonaComun;
        this.semaforo = new Semaphore(10000, true);
    }

    public void entrarZonaComun(Humano h){
        try {
            semaforo.acquire();
            listaDentroZonaComun.meterLista(h);
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis " + e);
        }
    }

    public void salirZonaComun(Humano h){
        try {
            listaDentroZonaComun.sacarLista(h);
            semaforo.release();
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis " + e);
        }
    }

}
