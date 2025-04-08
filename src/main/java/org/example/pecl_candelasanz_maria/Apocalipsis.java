package org.example.pecl_candelasanz_maria;

import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    private ListaHilos listaDentroZonaComun;
    private TextField HumanosZonaComun;
    private Semaphore semaforo;

    public Apocalipsis(TextField humanosZonaComun){
        this.HumanosZonaComun = humanosZonaComun;
        this.semaforo = new Semaphore(10000, true);
        this.listaDentroZonaComun = new ListaHilos(HumanosZonaComun);
    }

    public void entrarZonaComun(Humano h){
        try {
            semaforo.acquire();
            listaDentroZonaComun.meterLista(h);
            System.out.println("se añadio a la lista");
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
