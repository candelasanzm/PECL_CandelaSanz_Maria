package org.example.pecl_candelasanz_maria;

import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    private ListaHilos listaDentroZonaComun;
    private TextField HumanosZonaComun;
    private Semaphore semaforoZonaComun;

    private Tunel[] tuneles;

    private TextField[] EntradaT;
    private TextField[] Interiortunel;
    private TextField[] HumanosRiesgo;
    private TextField[] SalidaT;
    private TextField HumanosZonaDescanso;

    private ListaHilos[] listaEntradaT;
    private ListaHilos[] listaTunel;
    private ListaHilos[] listaHumanosRiesgo;
    private ListaHilos[] listaSalidaT;
    private ListaHilos listaDentroZonaDescanso;

    public Apocalipsis(TextField HumanosZonaComun, TextField[] EntradaT, TextField[] Interiortunel, TextField[] HumanosRiesgo, TextField[] SalidaT, TextField HumanosZonaDescanso ){
        this.HumanosZonaComun = HumanosZonaComun;
        this.semaforoZonaComun = new Semaphore(10000, true);
        this.listaDentroZonaComun = new ListaHilos(HumanosZonaComun);

        tuneles = new Tunel[4];
        for( int i = 0; i<4; i++){
            tuneles[i] = new Tunel(i+1,this); //id:1,2,3,4
        }

        listaEntradaT = new ListaHilos[4];
        for(int i=0; i<4;i++){
            listaEntradaT[i] = new ListaHilos(EntradaT[i]);
        }

        listaTunel = new ListaHilos[4];
        for(int i=0; i<4;i++){
            listaTunel[i] = new ListaHilos(Interiortunel[i]);
        }

        listaHumanosRiesgo = new ListaHilos[4];
        for(int i=0; i<4 ;i++){
            listaHumanosRiesgo[i] = new ListaHilos(HumanosRiesgo[i]);
        }

        listaSalidaT = new ListaHilos[4];
        for(int i=0; i<4;i++){
            listaSalidaT[i] = new ListaHilos(SalidaT[i]);
        }

        this.listaDentroZonaDescanso = new ListaHilos(HumanosZonaDescanso);
    }

    public void entrarZonaComun(Humano h){
        try {
            semaforoZonaComun.acquire();
            listaDentroZonaComun.meterLista(h);
            System.out.println("se añadio a la lista");
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Comun" + e);
        }
    }

    public void salirZonaComun(Humano h){
        try {
            listaDentroZonaComun.sacarLista(h);
            System.out.println("se elimino a la lista");
            semaforoZonaComun.release();
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Comun " + e);
        }
    }

    public void irTunel(int idTunel, Humano h){
        tuneles[idTunel-1].salirExterior(h);
    }

    public void irRefugio(int idTunel, Humano h){
        tuneles[idTunel-1].irRefugio(h);
    }

    public void meterEntradaTunel(int idTunel, Humano h){
        listaEntradaT[idTunel-1].meterLista(h);
    }
    public void meterSalidaTunel(int idTunel, Humano h){
        listaSalidaT[idTunel-1].meterLista(h);
    }
    public void meterTunelIda(int idTunel, Humano h){
        listaEntradaT[idTunel-1].sacarLista(h);
        listaTunel[idTunel-1].meterLista(h);
    }
    public void meterTunelVuelta(int idTunel, Humano h){
        listaSalidaT[idTunel-1].sacarLista(h);
        listaTunel[idTunel-1].meterLista(h);
    }
    public void meterRiesgoHumanos(int idTunel, Humano h){
        listaTunel[idTunel-1].sacarLista(h);
        listaHumanosRiesgo[idTunel-1].meterLista(h);
    }
    public void meterZonaDescanso(int idTunel, Humano h){
        listaTunel[idTunel-1].sacarLista(h);
        listaDentroZonaDescanso.meterLista(h);
    }
}
