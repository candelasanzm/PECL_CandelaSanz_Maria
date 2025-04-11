package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    private ListaHilosHumano listaDentroZonaComun;
    private TextField HumanosZonaComun;
    private Semaphore semaforoZonaComun;

    private Tunel[] tuneles;

    private TextField[] EntradaT;
    private TextField[] InteriorTunel;
    private TextField[] HumanosRiesgo;
    private TextField[] SalidaT;
    private TextField HumanosZonaDescanso;
    private TextField HumanosComedor;
    private TextField[] ZombiesRiesgo;

    private ListaHilosHumano[] listaEntradaT;
    private ListaHilosHumano[] listaTunel;
    private ListaHilosHumano[] listaHumanosRiesgo;
    private ListaHilosHumano[] listaSalidaT;
    private ListaHilosHumano listaDentroZonaDescanso;
    private ListaHilosHumano listaComedor;
    private ListaHilosZombie[] listaZombiesRiesgo;

    // Variables para las funciones de coger y dejar comida
    private int cantComida = 0;
    private TextField HumanosComida;


    public Apocalipsis(TextField HumanosZonaComun, TextField[] EntradaT, TextField[] InteriorTunel, TextField[] HumanosRiesgo, TextField[] SalidaT,
                       TextField HumanosZonaDescanso, TextField HumanosComedor, TextField HumanosComida, TextField[] ZombiesRiesgo) {
        this.HumanosZonaComun = HumanosZonaComun;
        this.semaforoZonaComun = new Semaphore(5000, true);
        this.listaDentroZonaComun = new ListaHilosHumano(HumanosZonaComun);
        this.listaDentroZonaDescanso = new ListaHilosHumano(HumanosZonaDescanso);
        this.listaComedor = new ListaHilosHumano(HumanosComedor);

        tuneles = new Tunel[4];
        for (int i = 0; i < 4; i++) {
            tuneles[i] = new Tunel(i + 1, this); //id:1,2,3,4
        }

        listaEntradaT = new ListaHilosHumano[4];
        for (int i = 0; i < 4; i++) {
            listaEntradaT[i] = new ListaHilosHumano(EntradaT[i]);
        }

        listaTunel = new ListaHilosHumano[4];
        for (int i = 0; i < 4; i++) {
            listaTunel[i] = new ListaHilosHumano(InteriorTunel[i]);
        }

        listaHumanosRiesgo = new ListaHilosHumano[4];
        for (int i = 0; i < 4; i++) {
            listaHumanosRiesgo[i] = new ListaHilosHumano(HumanosRiesgo[i]);
        }

        listaSalidaT = new ListaHilosHumano[4];
        for (int i = 0; i < 4; i++) {
            listaSalidaT[i] = new ListaHilosHumano(SalidaT[i]);
        }

        listaZombiesRiesgo = new ListaHilosZombie[4];
        for (int i = 0; i < 4; i++) {
            listaZombiesRiesgo[i] = new ListaHilosZombie(ZombiesRiesgo[i]);
        }

        this.HumanosComida = HumanosComida;
    }

    public void entrarZonaComun(Humano h) {
        try {
            semaforoZonaComun.acquire();
            listaDentroZonaComun.meterLista(h);
            System.out.println("Humano " + h.getID() + " entra a la zona común");
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Común" + e);
        }
    }

    public void salirZonaComun(Humano h) {
        try {
            listaDentroZonaComun.sacarLista(h);
            System.out.println("Humano " + h.getID() + " sale de la zona común");
            semaforoZonaComun.release();
        } catch (Exception e) {
            System.out.println("Error en Apocalipsis Zona Común " + e);
        }
    }

    public void irTunel(int idTunel, Humano h) {
        tuneles[idTunel - 1].salirExterior(h);
    }

    public void irRefugio(int idTunel, Humano h) {
        tuneles[idTunel - 1].irRefugio(h);
    }

    public void meterEntradaTunel(int idTunel, Humano h) { // en la interfaz el primer cuadrante
        listaEntradaT[idTunel - 1].meterLista(h);
    }

    public void meterSalidaTunel(int idTunel, Humano h) {
        listaSalidaT[idTunel - 1].meterLista(h);
    }

    public void meterTunelIda(int idTunel, Humano h) { // van de izquierda a derecha en el túnel
        listaEntradaT[idTunel - 1].sacarLista(h);
        listaTunel[idTunel - 1].meterLista(h);
    }

    public void meterTunelVuelta(int idTunel, Humano h) { // van de derecha a izquierda en el túnel
        listaSalidaT[idTunel - 1].sacarLista(h);
        listaTunel[idTunel - 1].meterLista(h);
    }

    public void meterRiesgoHumanos(int idTunel, Humano h) {
        listaTunel[idTunel - 1].sacarLista(h);
        listaHumanosRiesgo[idTunel - 1].meterLista(h);
    }

    public void meterZonaDescanso(int idTunel, Humano h) {
        listaTunel[idTunel - 1].sacarLista(h);
        listaDentroZonaDescanso.meterLista(h);
    }

    public void meterComedor(Humano h) {
        listaDentroZonaDescanso.sacarLista(h);
        listaComedor.meterLista(h);
    }
    public void salirComedor(Humano h) {  //ES POR SI NO HA SIDO ATACADO
        listaComedor.sacarLista(h);
    }

    public void meterZonaDescansoAtaque(Humano h) {
        listaComedor.sacarLista(h);
        listaDentroZonaDescanso.meterLista(h);
    }

    public void salirZonaDescansoAtaque(Humano h) {
        listaDentroZonaDescanso.sacarLista(h);
    }


    // Comida
    public synchronized void cogerComida(Humano h) throws InterruptedException { //coge comida del comedor
        while (cantComida < 2) {
            System.out.println(h.getID() + " espera porque no hay comida");
            wait();
        }
        cantComida--;
        System.out.println("Humano " + h.getID() + " coge comida");
        imprimirComida();
    }

    public synchronized void dejarComida(Humano h, int comida) throws InterruptedException {
        cantComida += comida;
        System.out.println(h.getID() + " añadió comida");
        imprimirComida();
        notifyAll();
    }

    public void imprimirComida() {
        Platform.runLater(() -> {HumanosComida.setText(String.valueOf(cantComida));
        });
    }

    public void moverZonaZombie(Zombie z, int zona){
        int zonaAnterior = z.getZona();
        if(zonaAnterior != -1){ //si ya tiene zona se elimina de la zona en la que estaba
            listaZombiesRiesgo[zonaAnterior].sacarLista(z);
        }
        listaZombiesRiesgo[zona].meterLista(z);
        z.setZona(zona); //actualizamos la nueva zona
        System.out.println(z.getID() + " se ha movido de zona");
    }
}
