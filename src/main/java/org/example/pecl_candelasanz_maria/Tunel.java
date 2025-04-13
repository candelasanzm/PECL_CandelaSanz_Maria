package org.example.pecl_candelasanz_maria;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Tunel {
    private Apocalipsis ap;
    private Semaphore semaforoTunel; // controla que el acceso al tunel sea de uno en uno
    private int id; // identificador del tunel
    private CyclicBarrier b; // permite que los humanos puedan pasar en grupos
    private final LinkedBlockingQueue colaTunel = new LinkedBlockingQueue<>(); // sin limite

    public Tunel(int id, Apocalipsis ap){
        this.id = id;
        this.ap = ap;
        this.semaforoTunel = new Semaphore(1); // entran de uno en uno
        this.b = new CyclicBarrier(3); // pasan en grupos de 3
    }

    public void salirExterior(Humano h){
        try {
            // Humano entra en túnel
            System.out.println("Humano " + h.getID() + " intenta entrar al túnel " + id);
            //Mover a la entrada del tunel
            ap.moverHumano(ap.getZonas(id), h);

            //PRIORIDAD
            synchronized (this) {
                while (!colaTunel.isEmpty()) {
                    System.out.println(h.getID() + " espera porque hay prioridad");
                    wait();
                }
            }

            System.out.println("Humano " + h.getID() + " está esperando para formar grupo en el túnel " + id);
            b.await(); //espera a que haya 3 humanos
            System.out.println("Grupo formado en el túnel " + id);

            //Entra al tunel
            semaforoTunel.acquire(); // acceden al túnel de uno en uno
            int tunelInterior = 7 + (id - 3); //Zonas 7,8,9,10
            ap.moverHumano(ap.getZonas(tunelInterior), h);
            System.out.println(h.getID() + " atraviesa túnel" + id);
            Thread.sleep(1000); // tiempo de cruce
            semaforoTunel.release(); // se libera el acceso al túnel

            //mover zona riesgo
            int zonaRiesgo = 15 + (id - 3); //Zonas 15,16,17,18
            ap.moverHumano(ap.getZonas(zonaRiesgo), h);
            System.out.println("Humano " + h.getID() + " entra en la zona de riesgo " + id);


        } catch (Exception e) {
            System.out.println("Error en túnel " + e.getMessage());
        }
    }

    public void irRefugio(Humano h){
        try {
            System.out.println(h.getID() + " regresa a la zona segura desde el túnel " + id);
            colaTunel.put(h); //se mete en la cola de salida

            //entra a la  salida
            int zonaSalida = 11 + (id - 3); //zonas 11,12,13,14
            ap.moverHumano(ap.getZonas(zonaSalida), h);

            semaforoTunel.acquire(); //de uno en uno
            colaTunel.remove(); //cruza el tunel

            //entra al interior del tunel
            int tunelInterior = 7 + (id - 3); //Zonas 7,8,9,10
            ap.moverHumano(ap.getZonas(tunelInterior), h);
            System.out.println("Humano " + h.getID() + " entra en túnel de vuelta");
            Thread.sleep(1000);
            semaforoTunel.release();

            //Mover Zona descanso
            ap.moverHumano(ap.getZonas(1), h);

            synchronized (this) {
                notifyAll();
            }

        } catch (Exception e) {
            System.out.println("Error en túnel " + e);
        }
    }
}
