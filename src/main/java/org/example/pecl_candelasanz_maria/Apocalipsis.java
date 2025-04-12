package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    // Zonas
    private Zona zonaComun;
    private Zona zonaDescanso;
    private Zona comedor;
    private Zona tunel1;
    private Zona tunel2;
    private Zona tunel3;
    private Zona tunel4;
    private Zona zonaRiesgo1;
    private Zona zonaRiesgo2;
    private Zona zonaRiesgo3;
    private Zona zonaRiesgo4;
    private Zona[] zonas; // Creo un array para almacenar las distintas zonas

    // Variables Humanos
    private ListaHilosHumano[] listaHumanos; // lista donde vamos a manejar todos los humanos
    private TextField[] zonasTxtField; // array de textfields para la interfaz
    private int cantComida = 0; // vemos cuánta comida hay
    private TextField HumanosComida;

    // Variables Zombies
    private ListaHilosZombie[] listaZombies;
    private TextField[] zombiesTxtField;

    // Semáforo para controlar las distintas zonas
    //private Semaphore[] semaforosZonas;

    /*private ListaHilosHumano listaDentroZonaComun;
    private TextField HumanosZonaComun;
    private Semaphore semaforoZonaComun;

    private Tunel[] tuneles;

    private TextField[] EntradaT;
    private TextField[] InteriorTunel;
    private TextField[] HumanosRiesgo;
    private TextField[] SalidaT;
    private TextField[] HumanosZonaDescanso;
    private TextField[] HumanosComedor;
    private TextField[] ZombiesRiesgo;

    private ListaHilosHumano[] listaHumanos;
    private ListaHilosHumano[] listaEntradaT;
    private ListaHilosHumano[] listaTunel;
    private ListaHilosHumano[] listaHumanosRiesgo;
    private ListaHilosHumano[] listaSalidaT;
    private ListaHilosHumano[] listaDentroZonaDescanso;
    private ListaHilosHumano[] listaComedor;
    private ListaHilosZombie[] listaZombiesRiesgo;

    // Variables para las funciones de coger y dejar comida
    private int cantComida = 0;
    private TextField HumanosComida;*/

    public Apocalipsis(TextField[] zonasTxtField, TextField humanosComida, TextField[] zombiesTxtField) {
        this.zonasTxtField = zonasTxtField;
        this.HumanosComida = humanosComida;
        this.zombiesTxtField = zombiesTxtField;

        zonas = new Zona[11]; // array para almacenar las distintas zonas
        listaHumanos = new ListaHilosHumano[11]; // array que almacena "sublistas", listas de cada zona, es decir, si tengo zona[0], listaHumanos[0] será la lista de humanos que se encuentren en la zona 0
        listaZombies = new ListaHilosZombie[4]; // los zombies solo se mueven por las zonas de riesgo
        //semaforosZonas = new Semaphore[11]; // tenemos un semáforo para cada zona

        zonas[0] = new Zona(0, "zonaComun");
        zonas[1] = new Zona(1, "zonaDescanso");
        zonas[2] = new Zona(2, "comedor");
        zonas[3] = new Zona(3, "tunel1");
        zonas[4] = new Zona(4, "tunel2");
        zonas[5] = new Zona(5, "tunel3");
        zonas[6] = new Zona(6, "tunel4");
        zonas[7] = new Zona(7, "zonaRiesgo1");
        zonas[8] = new Zona(8, "zonaRiesgo2");
        zonas[9] = new Zona(9, "zonaRiesgo3");
        zonas[10] = new Zona(10, "zonaRiesgo4");

        for (int i = 0; i < zonas.length; i++) {
            listaHumanos[i] = new ListaHilosHumano(zonasTxtField[i]);
        }

        for (int i = 7; i < 11; i++) { // ya que los zombies solo se mueven por las zonas de riesgo que tienen ids del 7 al 10
            listaZombies[i - 7] = new ListaHilosZombie(zombiesTxtField[i - 7]);
        }
    }

    /*public Apocalipsis(TextField HumanosZonaComun, TextField[] EntradaT, TextField[] InteriorTunel, TextField[] HumanosRiesgo, TextField[] SalidaT,
                       TextField[] HumanosZonaDescanso, TextField[] HumanosComedor, TextField HumanosComida, TextField[] ZombiesRiesgo) {
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

    // Funciones para el ataque
    public void ataque(){
        int posibilidadSobrevivir = (int) (Math.random() * 3) + 1; // crea un número aleatorio entre 1, 2 y 3

        if(posibilidadSobrevivir < 3){

        } else { // si sale 3 el humano muere y se convierte en zombie

        }
    }
}
*/

    // Movimiento entre zonas
    public void entrarZona(int idZona, Humano h){
        try {
            //semaforosZonas[idZona].acquire();
            listaHumanos[idZona].meterLista(h);
            System.out.println("Humano " + h.getID() + " entra a " + zonas[idZona].getNombre());
        } catch (Exception e) {
            System.out.println("Error al entrar a la zona " + e.getMessage());
        }
    }

    public void salirZona(int idZona, Humano h) {
        try {
            listaHumanos[idZona].sacarLista(h);
            System.out.println("Humano " + h.getID() + " sale de " + zonas[idZona].getNombre());
            //semaforosZonas[idZona].release();
        } catch (Exception e) {
            System.out.println("Error al salir de la zona " + e.getMessage());
        }
    }

    public void moverHumano(int zonaOrigen, int zonaDestino, Humano h) {
        try {
            salirZona(zonaOrigen, h);
            entrarZona(zonaDestino, h);
            System.out.println("Humano " + h.getID() + " se movió de " + zonas[zonaOrigen].getNombre() + " a " + zonas[zonaDestino].getNombre());
        } catch (Exception e) {
            System.out.println("Error al mover humano " + e.getMessage());
        }
    }

    // Comida
    public synchronized void cogerComida(Humano h) throws InterruptedException { // coge comida del comedor y come
        while (cantComida == 0) {
            System.out.println(h.getID() + " espera porque no hay comida");
            wait();
        }
        cantComida--;
        System.out.println("Humano " + h.getID() + " coge comida y come");
        imprimirComida();
    }

    public synchronized void dejarComida(Humano h, int comida) throws InterruptedException {
        cantComida += comida;
        System.out.println(h.getID() + " añadió comida");
        imprimirComida();
        notifyAll();
    }

    public void imprimirComida() { // imprime en la interfaz la cantidad de comida que hay
        Platform.runLater(() -> {HumanosComida.setText(String.valueOf(cantComida));
        });
    }

    // Movimiento de los zombies entre zonas
    public void moverZonaZombie(Zombie z, int zona){
        // Limito el movimiento del zombie entre las zonas de riesgo
        if (zona < 7 || zona > 10){
            System.out.println("Zombie " + z.getID() + " no puede moverse a esa zona");
        }

        int zonaAnterior = z.getZona();
        try {
            if(zonaAnterior != -1){ // si ya tiene zona se elimina de la zona en la que estaba
                listaZombies[zonaAnterior - 7].sacarLista(z);
            }

            listaZombies[zona - 7].meterLista(z);
            z.setZona(zona); // actualizamos la nueva zona
            System.out.println("Zombie " + z.getID() + " se ha movido a zona " + zonas[zona].getNombre());
        } catch (Exception e) {
            System.out.println("Error al mover zombie " + e.getMessage());
        }
    }
}