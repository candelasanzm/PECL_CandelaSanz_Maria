package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Apocalipsis {
    // Zonas
    private Zona[] zonas; // Creo una lista para almacenar las distintas zonas

    // Variables Humanos
    private ListaHilosHumano[] listaHumanos; // Lista donde vamos a manejar todos los humanos
    private TextField[] zonasTxtField; // Lista de Textfields para la interfaz
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

    // Botón de parada
    private Paso paso = new Paso();

    public Apocalipsis(TextField[] zonasTxtField, TextField humanosComida, TextField[] zombiesTxtField) {
        this.zonasTxtField = zonasTxtField;
        this.HumanosComida = humanosComida;
        this.zombiesTxtField = zombiesTxtField;

        zonas = new Zona[19]; // Lista para almacenar las distintas zonas
        listaHumanos = new ListaHilosHumano[19]; // Lista que almacena "sublistas", listas de cada zona, es decir, si tengo zona[0], listaHumanos[0] será la lista de humanos que se encuentren en la zona 0
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

    public ListaHilosZombie[] getListaZombies() {
        return listaZombies;
    }

    public Zona getZonas(int zona) {
        return zonas[zona];
    }

    public Tunel getTunel(int idTunel){
        return tuneles[idTunel];
    }

    public Paso getPaso() {
        return paso;
    }

    // Humano empieza en Zona Común
    public synchronized void empiezaZonaComun(Zona zona, Humano h){
        apocalipsisLogs.registrarEvento("Humano " + h.getID() + " está en la Zona Común");
        listaHumanos[zona.getIdZona()].meterLista(h);
        h.setZona(zona);
    }

    // Movimiento entre zonas
    public synchronized void moverHumano(Zona zonaDestino, Humano h) {
        Zona zonaActual = h.getZona();

        apocalipsisLogs.registrarEvento("Humano " + h.getID() + " se movió de " + h.getZona().getNombre() + " a " + zonaDestino.getNombre());
        listaHumanos[zonaActual.getIdZona()].sacarLista(h); // Saca de la zona anterior y actualiza interfaz
        if (!Thread.currentThread().isInterrupted() && h.isVivo()){ // Verificamos que el humano esté vivo antes de entrar en la lista
            listaHumanos[zonaDestino.getIdZona()].meterLista(h); // Mete en la nueva zona y actualiza interfaz
        }
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

    public void imprimirComida() { // Imprime en la interfaz la cantidad de comida que hay
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
            if(zonaAnterior != -1){ // Si ya tiene zona se elimina de la zona en la que estaba
                listaZombies[zonaAnterior - 15].sacarLista(z);
            }

            listaZombies[zona - 15].meterLista(z);
            z.setZona(zona); // Actualizamos la nueva zona
            apocalipsisLogs.registrarEvento("Zombie " + z.getID() + " se ha movido a " + zonas[zona].getNombre());
        }
    }

    // Funciones relacionadas con el ataque para el humano
    public void defenderse(Humano humano, Zombie zombie){
        int posibilidad = (int) (Math.random() * 3) + 1; // La posibilidad de supervivencia será de 1, 2 o 3

        if (posibilidad < 3){ // Si sale uno o dos vive, pero queda marcado
            humano.setMarcado(true);
            apocalipsisLogs.registrarEvento("Humano " + humano.getID() + " se ha defendido exitosamente y está marcado por el ataque del zombie " + zombie.getID());
        } else { // Si sale 3 es false
            humano.setVivo(false);
            apocalipsisLogs.registrarEvento("El humano " + humano.getID() + " no ha podido defenderse del ataque del zombie " + zombie.getID() + " y muere. Renace como Zombie ");
        }
    }

    // Función para el elegir el objetivo al que ataca
    private Humano obtenerHumano(ListaHilosHumano listaZona){
        // Sincronizamos el acceso a la lista para evitar los problemas de concurrencia
        synchronized (listaZona){
            int tamanoLista = listaZona.getListado().size();
            if (tamanoLista == 0){
                return null;
            }

            // Probar N veces (N = tamaño de la lista) con índices aleatorios
            boolean[] intentados = new boolean[tamanoLista];
            int intentos = 0; // Variable para que no intente atacar a más humanos de los que hay disponibles en su zona

            while (intentos < tamanoLista){
                // Se selecciona un índice aleatorio dentro del tamaño de la lista
                int indice = (int) (Math.random() * tamanoLista);
                if (intentados[indice]){
                    continue;
                }

                // Marcamos el índice como probado
                intentados[indice] = true;

                // Seleccionamos al humano en la posición llamada índice
                Humano objetivo = listaZona.getListado().get(indice);

                // Intentamos bloquear el objetivo para que ningún otro zombie le ataque
                if(objetivo.getCerrojoAtaque().tryLock()){
                    return objetivo; // Si se puede bloquear, devolvemos el objetivo
                }

                // Se añade un intento
                intentos ++;
            }
        }

        return null;
    }

    // Funciones relacionadas con el ataque para el zombie
    public synchronized void comprobarParaAtacar(Zombie zombie, Zona zona) {
        Humano objetivo = null;

        try{
            objetivo = obtenerHumano(listaHumanos[zona.getIdZona()]);

            if (objetivo == null){
                apocalipsisLogs.registrarEvento("Zombie " + zombie.getID() + " no encontró humanos en " + zona.getNombre());
            } else {
                apocalipsisLogs.registrarEvento("Zombie " + zombie.getID() + " ataca al humano " + objetivo.getID() + " en " + zona.getNombre());

                // El ataque dura entre 0.5 y 1.5 segundos
                sleep((int) (Math.random() * 1000) + 500);

                defenderse(objetivo, zombie); // Vemos si el humano se defiende

                // Comprobamos que pasa con el humano después del ataque
                if (!objetivo.isVivo()) {
                    Zona zonaActualHumano = objetivo.getZona();

                    synchronized (listaHumanos[zonaActualHumano.getIdZona()]){
                        // Elimina al humano de la lista
                        listaHumanos[zonaActualHumano.getIdZona()].sacarLista(objetivo);
                    }

                    // Interrumpimos el hilo para evitar que se siga ejecutando
                    objetivo.interrupt();

                    zombie.anadirMuerte();
                    renacerComoZombie(objetivo, zona);
                } else if (objetivo.isMarcado()) {
                    apocalipsisLogs.registrarEvento("Humano " + objetivo.getID() + " logró defenderse y ha quedado marcado");
                }
            }
        } catch (Exception e){
            apocalipsisLogs.registrarEvento("Error durante el ataque " + e.getMessage());
        } finally {
            if (objetivo!= null){
                objetivo.getCerrojoAtaque().unlock();
            }
        }
    }

    public synchronized void renacerComoZombie(Humano h, Zona zona){
        // Crear zombie
        String id = h.getID().replace("H", "Z");
        Zombie z = new Zombie(this, id);

        moverZonaZombie(z, zona.getIdZona());
        apocalipsisLogs.registrarEvento("Zombie " + id + " ha renacido en la zona " + zona.getNombre());
        z.start();
    }
}