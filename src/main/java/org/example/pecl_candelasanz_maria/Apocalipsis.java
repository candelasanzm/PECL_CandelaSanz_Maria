package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Apocalipsis {
    // Zonas
    private Zona[] zonas; // Creo un array para almacenar las distintas zonas

    // Variables Humanos
    private ListaHilosHumano[] listaHumanos; // Lista donde vamos a manejar todos los humanos
    private TextField[] zonasTxtField; // Array de Textfields para la interfaz
    private int cantComida = 0; // Vemos cuánta comida hay, "almacén" de comida
    private TextField HumanosComida;
    private Lock comidaLock = new ReentrantLock();
    private Condition comidaDisponible = comidaLock.newCondition();

    // Variables Zombies
    private ListaHilosZombie[] listaZombies;
    private TextField[] zombiesTxtField;

    // Tuneles
    private Tunel[] tuneles;

    // Inicialización de los log para escribirlos en un archivo txt
    private ApocalipsisLogs apocalipsisLogs = ApocalipsisLogs.getInstancia();

    public Apocalipsis(TextField[] zonasTxtField, TextField humanosComida, TextField[] zombiesTxtField) {
        this.zonasTxtField = zonasTxtField;
        this.HumanosComida = humanosComida;
        this.zombiesTxtField = zombiesTxtField;

        zonas = new Zona[19]; // Array para almacenar las distintas zonas
        listaHumanos = new ListaHilosHumano[19]; // Array que almacena "sublistas", listas de cada zona, es decir, si tengo zona[0], listaHumanos[0] será la lista de humanos que se encuentren en la zona 0
        listaZombies = new ListaHilosZombie[4]; // Los zombies solo se mueven por las zonas de riesgo

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

        for (int i = 15; i < 19; i++) { // Ya que los zombies solo se mueven por las zonas de riesgo que tienen ids del 15 al 18
            listaZombies[i - 15] = new ListaHilosZombie(zombiesTxtField[i - 15]);
        }

        tuneles = new Tunel[4];
        for(int i = 0; i < 4; i++){
            tuneles[i]=new Tunel(3 + i, this); // Id = 3, 4, 5, 6
        }
    }

    // Getter para extraer los datos necesarios
    public ListaHilosHumano getListaHumanosEnZona(int zona) {
        return listaHumanos[zona];
    }

    public Zona getZonas(int zona) {
        return zonas[zona];
    }

    public Tunel getTunel(int idTunel){
        return tuneles[idTunel];
    }

    // Movimiento entre zonas
    public synchronized void moverHumano(Zona zonaDestino, Humano h) {
        Zona zonaActual = h.getZona();

        apocalipsisLogs.registrarEvento("Humano " + h.getID() + " se movió de " + h.getZona().getNombre() + " a " + zonaDestino.getNombre());
        listaHumanos[zonaActual.getIdZona()].sacarLista(h); //saca de la zona anterior y actualiza interfaz
        listaHumanos[zonaDestino.getIdZona()].meterLista(h); //mete en la nueva zona y actualiza interfaz
        h.setZona(zonaDestino);
    }

    // Comida
    public void cogerComida(Humano h) throws InterruptedException { // Coge comida del comedor y come
        comidaLock.lock();
        try {
            while (cantComida == 0) {
                apocalipsisLogs.registrarEvento(h.getID() + " espera porque no hay comida");
                comidaDisponible.await();
            }
            cantComida--;
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " coge comida y va a comer. Quedan " + cantComida + " piezas de comida");
            imprimirComida();
        } finally {
            comidaLock.unlock();
        }
    }

    public void dejarComida(Humano h, int comida){
        comidaLock.lock();
        try {
            cantComida += comida;
            apocalipsisLogs.registrarEvento(h.getID() + " añadió 2 piezas de comida");
            imprimirComida();
            comidaDisponible.signal();
        } finally {
            comidaLock.unlock();
        }
    }

    public void imprimirComida() { // imprime en la interfaz la cantidad de comida que hay
        Platform.runLater(() -> {HumanosComida.setText(String.valueOf(cantComida));});
    }

    // Movimiento de los zombies entre zonas
    public void moverZonaZombie(Zombie z, int zona){
        // Limito el movimiento del zombie entre las zonas de riesgo
        if (zona < 15 || zona > 18){
            apocalipsisLogs.registrarEvento("Zombie " + z.getID() + " no puede moverse a esa zona");
            return ; // Se para aquí en ved de seguir ejecutándose
        }

        int zonaAnterior = z.getZona();
        synchronized (this){
            if(zonaAnterior != -1){ // si ya tiene zona se elimina de la zona en la que estaba
                listaZombies[zonaAnterior - 15].sacarLista(z);
            }

            listaZombies[zona - 15].meterLista(z);
            z.setZona(zona); // actualizamos la nueva zona
            apocalipsisLogs.registrarEvento("Zombie " + z.getID() + " se ha movido a zona " + zonas[zona].getNombre());
        }
    }

    // Funciones relacionadas con el ataque para el humano
    public boolean isDefendido(){ //devuelve true cuando se salva
        int posibilidad = (int) (Math.random() * 3) + 1; // la posibilidad de supervivencia será de 1, 2 o 3
        return posibilidad < 3; //Si sale 3 es false
    }

    public void defenderse(Humano humano, Zombie zombie){
        if (isDefendido()){
            humano.setMarcado(true);
            apocalipsisLogs.registrarEvento("Humano " + humano.getID() + " se ha defendido exitosamente y está marcado por el ataque del zombie " + zombie.getID());
        } else {
            humano.setVivo(false);
            apocalipsisLogs.registrarEvento("El humano " + humano.getID() + " no ha podido defenderse del ataque del zombie " + zombie.getID() + " y muere. Renace como Zombie ");
        }
    }

    // Funciones relacionadas con el ataque para el zombie
    public synchronized void comprobarParaAtacar(Zombie zombie, Zona zona){

        if (listaHumanos[zona.getIdZona()].getListado().isEmpty()){ // Compruebo si la lista es vacía porque entonces el zombie no puede atacar
            apocalipsisLogs.registrarEvento("No hay humanos en " + zona.getNombre() + " el zombie " + zombie.getID() + " no puede atacar");

            try { // Espera entre 2 y 3 segundos antes de cambiar de zona
                Thread.sleep((int) (Math.random() * 1000) + 2000);
            } catch (InterruptedException e) {
                apocalipsisLogs.registrarEvento("Error al esperar humanos " + e.getMessage());
            }
            return; // Como no hay humanos sale
        }

        int idHumano = (int) (Math.random() * listaHumanos[zona.getIdZona()].getListado().size()); // cojo un humano al azar de entre los que hay en la zona
        Humano objetivo = listaHumanos[zona.getIdZona()].getListado().get(idHumano);

        apocalipsisLogs.registrarEvento("Zombie " + zombie.getID() + " ataca al humano " + objetivo.getID() + " en zona " + zona.getNombre());

        // El ataque dura entre 0.5 y 1.5 segundos
        try {
            Thread.sleep((int) (Math.random() * 1000) + 500);
        } catch (InterruptedException e) {
            apocalipsisLogs.registrarEvento("Error durante el ataque " + e.getMessage());
        }

        defenderse(objetivo, zombie); // vemos si el humano se defiende

        // Comprobamos que pasa con el humano después del ataque
        if (!objetivo.isVivo()){
            //Elimina al humano de la lista
            listaHumanos[zona.getIdZona()].sacarLista(objetivo);
            zombie.anadirMuerte();
            renacerComoZombie(objetivo,zona);
        } else if(objetivo.isMarcado()) {
            apocalipsisLogs.registrarEvento("Humano " + objetivo.getID() + " logró defenderse y ha quedado marcado");
        }
    }

    public void renacerComoZombie(Humano h, Zona zona){
        // Eliminar el humano antes de crear el zombie
        listaHumanos[zona.getIdZona()].sacarLista(h);

        // Crear zombie
        String id = h.getID().replace("H", "Z");
        Zombie z = new Zombie(this,id);

        moverZonaZombie(z, zona.getIdZona());
        apocalipsisLogs.registrarEvento("Zombie " + id + " ha renacido en la zona " + zona.getNombre());
        z.start();
    }

    // Contar humanos en zonas
    public void recuentoHumanos(){
        for (Zona zona : zonas) {
            int cantHumanos = listaHumanos[zona.getIdZona()].getListado().size();

            apocalipsisLogs.registrarEvento(zona.getNombre() + " tiene " + cantHumanos + " humanos");
        }
    }

    // Contar Zombies en zonas
    public void recuentoZombies(){
        for (Zona zona : zonas) {
            int cantZombies = listaZombies[zona.getIdZona()].getListado().size();

            apocalipsisLogs.registrarEvento(zona.getNombre() + " tiene " + cantZombies + " zombies");
        }
    }
}