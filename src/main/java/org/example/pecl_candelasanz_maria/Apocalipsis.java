package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;

public class Apocalipsis {
    // Zonas
    private Zona zonaComun;
    private Zona zonaDescanso;
    private Zona comedor;
    private Zona entradaTunel1;
    private Zona entradaTunel2;
    private Zona entradaTunel3;
    private Zona entradaTunel4;
    private Zona tunel1;
    private Zona tunel2;
    private Zona tunel3;
    private Zona tunel4;
    private Zona salidaTunel1;
    private Zona salidaTunel2;
    private Zona salidaTunel3;
    private Zona salidaTunel4;
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

    //Tuneles
    private Tunel[] tuneles;

    // Semáforo para controlar las distintas zonas
    //private Semaphore[] semaforosZonas;

    public Apocalipsis(TextField[] zonasTxtField, TextField humanosComida, TextField[] zombiesTxtField) {
        this.zonasTxtField = zonasTxtField;
        this.HumanosComida = humanosComida;
        this.zombiesTxtField = zombiesTxtField;

        zonas = new Zona[19]; // array para almacenar las distintas zonas
        listaHumanos = new ListaHilosHumano[19]; // array que almacena "sublistas", listas de cada zona, es decir, si tengo zona[0], listaHumanos[0] será la lista de humanos que se encuentren en la zona 0
        listaZombies = new ListaHilosZombie[4]; // los zombies solo se mueven por las zonas de riesgo
        //semaforosZonas = new Semaphore[19]; // tenemos un semáforo para cada zona

        zonas[0] = new Zona(0, "Zona Común");
        zonas[1] = new Zona(1, "Zona Descanso");
        zonas[2] = new Zona(2, "Comedor");
        zonas[3] = new Zona(3, "Entrada Túnel 1");
        zonas[4] = new Zona(4, "Entrada Túnel 2");
        zonas[5] = new Zona(5, "Entrada Túnel 3");
        zonas[6] = new Zona(6, "Entrada Túnel 4");
        zonas[7] = new Zona(7, "Túnel 1");
        zonas[8] = new Zona(8, "Túnel 2");
        zonas[9] = new Zona(9, "Túnel 3");
        zonas[10] = new Zona(10, "Túnel 4");
        zonas[11] = new Zona(11, "Salida Túnel 1");
        zonas[12] = new Zona(12, "Salida Túnel 2");
        zonas[13] = new Zona(13, "Salida Túnel 3");
        zonas[14] = new Zona(14, "Salida Túnel 4");
        zonas[15] = new Zona(15, "Zona Riesgo 1");
        zonas[16] = new Zona(16, "Zona Riesgo 2");
        zonas[17] = new Zona(17, "Zona Riesgo 3");
        zonas[18] = new Zona(18, "Zona Riesgo 4");

        for (int i = 0; i < zonas.length; i++) {
            listaHumanos[i] = new ListaHilosHumano(zonasTxtField[i]);
        }

        for (int i = 15; i < 19; i++) { // ya que los zombies solo se mueven por las zonas de riesgo que tienen ids del 15 al 18
            listaZombies[i - 15] = new ListaHilosZombie(zombiesTxtField[i - 15]);
        }

        tuneles = new Tunel[4];
        for(int i = 0; i < 4; i++){
            tuneles[i]=new Tunel(3 + i, this); //id=3,4,5,6
        }
    }

    public Zona getZonas(int zona) {
        return zonas[zona];
    }


    public Tunel getTunel(int idTunel){
        return tuneles[idTunel];
    }

    // Movimiento entre zonas
    public void moverHumano(Zona zonaDestino, Humano h) {
        try {
            Zona zonaActual = h.getZona();
            if (zonaActual != null) {
                listaHumanos[zonaActual.getIdZona()].sacarLista(h); //saca de la zona anterior y actualiza interfaz
            }

            System.out.println("Humano " + h.getID() + " se movió de " + h.getZona().getNombre() + " a " + zonaDestino.getNombre());
            listaHumanos[zonaDestino.getIdZona()].meterLista(h); //mete en la nueva zona y actualiza interfaz
            h.setZona(zonaDestino);
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
        System.out.println("Humano " + h.getID() + " coge comida y va a comer. Quedan " + cantComida + " piezas de comida");
        imprimirComida();
    }

    public synchronized void dejarComida(Humano h, int comida) throws InterruptedException {
        cantComida += comida;
        System.out.println(h.getID() + " añadió comida");
        imprimirComida();
        notifyAll();
    }

    public void imprimirComida() { // imprime en la interfaz la cantidad de comida que hay
        Platform.runLater(() -> {HumanosComida.setText(String.valueOf(cantComida));});
    }

    // Movimiento de los zombies entre zonas
    public void moverZonaZombie(Zombie z, int zona){
        // Limito el movimiento del zombie entre las zonas de riesgo
        if (zona < 15 || zona > 18){
            System.out.println("Zombie " + z.getID() + " no puede moverse a esa zona");
        }

        int zonaAnterior = z.getZona();
        try {
            if(zonaAnterior != -1){ // si ya tiene zona se elimina de la zona en la que estaba
                listaZombies[zonaAnterior - 15].sacarLista(z);
            }

            listaZombies[zona - 15].meterLista(z);
            z.setZona(zona); // actualizamos la nueva zona
            System.out.println("Zombie " + z.getID() + " se ha movido a zona " + zonas[zona].getNombre());
        } catch (Exception e) {
            System.out.println("Error al mover zombie " + e.getMessage());
        }
    }

    // Contar humanos en zonas
    public void recuentoHumanos(){
        for (Zona zona : zonas) {
            int cantHumanos = listaHumanos[zona.getIdZona()].getListado().size();

            System.out.println(zona.getNombre() + " tiene " + cantHumanos + " humanos");
        }
    }

}