package org.example.pecl_candelasanz_maria;

import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    private ListaHilos listaDentroZonaComun;
    private TextField HumanosZonaComun;
    private Semaphore semaforoZonaComun;

    private Tunel[] tuneles;

    private TextField[] EntradaT;
    private TextField[] InteriorTunel;
    private TextField[] HumanosRiesgo;
    private TextField[] SalidaT;
    private TextField HumanosZonaDescanso;

    private ListaHilos[] listaEntradaT;
    private ListaHilos[] listaTunel;
    private ListaHilos[] listaHumanosRiesgo;
    private ListaHilos[] listaSalidaT;
    private ListaHilos listaDentroZonaDescanso;

    // Variables para las funciones de coger y dejar comida
    private int maxComida;
    private int[] comidas;
    private int numComida = 0;
    private int in = 0;
    private int out = 0;
    private int almacenComida = 0;

    public Apocalipsis(TextField HumanosZonaComun, TextField[] EntradaT, TextField[] InteriorTunel, TextField[] HumanosRiesgo, TextField[] SalidaT,
                       TextField HumanosZonaDescanso){
        this.HumanosZonaComun = HumanosZonaComun;
        this.semaforoZonaComun = new Semaphore(5000, true);
        this.listaDentroZonaComun = new ListaHilos(HumanosZonaComun);

        tuneles = new Tunel[4];
        for(int i = 0; i < 4; i++){
            tuneles[i] = new Tunel(i + 1,this); //id:1,2,3,4
        }

        listaEntradaT = new ListaHilos[4];
        for(int i = 0; i < 4;i++){
            listaEntradaT[i] = new ListaHilos(EntradaT[i]);
        }

        listaTunel = new ListaHilos[4];
        for(int i = 0; i < 4;i++){
            listaTunel[i] = new ListaHilos(InteriorTunel[i]);
        }

        listaHumanosRiesgo = new ListaHilos[4];
        for(int i = 0; i < 4 ;i++){
            listaHumanosRiesgo[i] = new ListaHilos(HumanosRiesgo[i]);
        }

        listaSalidaT = new ListaHilos[4];
        for(int i = 0; i < 4;i++){
            listaSalidaT[i] = new ListaHilos(SalidaT[i]);
        }
        this.listaDentroZonaDescanso = new ListaHilos(HumanosZonaDescanso);

        this.maxComida = 2;
        comidas = new int[maxComida];
    }

    public void entrarZonaComun(Humano h){
        try {
            semaforoZonaComun.acquire();
            listaDentroZonaComun.meterLista(h);
            System.out.println("Humano " + h.getID() + " se añadió a la lista");
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Común" + e);
        }
    }

    public void salirZonaComun(Humano h){
        try {
            listaDentroZonaComun.sacarLista(h);
            System.out.println("Humano " + h.getID() + " se eliminó de la lista");
            semaforoZonaComun.release();
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Común " + e);
        }
    }

    public void irTunel(int idTunel, Humano h){
        tuneles[idTunel - 1].salirExterior(h);
    }

    public void irRefugio(int idTunel, Humano h){
        tuneles[idTunel - 1].irRefugio(h);
    }

    public void meterEntradaTunel(int idTunel, Humano h){ // en la interfaz el primer cuadrante
        listaEntradaT[idTunel-1].meterLista(h);
    }

    public void meterSalidaTunel(int idTunel, Humano h){
        listaSalidaT[idTunel - 1].meterLista(h);
    }

    public void meterTunelIda(int idTunel, Humano h){ // van de izquierda a derecha en el túnel
        listaEntradaT[idTunel - 1].sacarLista(h);
        listaTunel[idTunel - 1].meterLista(h);
    }

    public void meterTunelVuelta(int idTunel, Humano h){ // van de derecha a izquierda en el túnel
        listaSalidaT[idTunel - 1].sacarLista(h);
        listaTunel[idTunel - 1].meterLista(h);
    }

    public void meterRiesgoHumanos(int idTunel, Humano h){
        listaTunel[idTunel - 1].sacarLista(h);
        listaHumanosRiesgo[idTunel - 1].meterLista(h);
    }

    public void meterZonaDescanso(int idTunel, Humano h){
        listaTunel[idTunel - 1].sacarLista(h);
        listaDentroZonaDescanso.meterLista(h);
    }

    // Comida
    public synchronized int[] cogerComida() throws InterruptedException{
        while (numComida < 2){
            wait();
        }

        int[] comida = new int[2];
        for (int i = 0; i < 2; i++){
            comidas[i] = comidas[out];
            comidas[out] = -1;
            out = (out + 1) % maxComida;
            numComida--;
        }
        notifyAll();
        return comida;
    }

    public synchronized void dejarComida(int comida) throws InterruptedException{
        while (numComida == maxComida){
            wait();
        }

        comidas[in] = comida;
        numComida++;
        in = (in + 1) % maxComida;
        System.out.println("Se añadió la comida ");
        almacenComida++;
        notifyAll();
    }
}
