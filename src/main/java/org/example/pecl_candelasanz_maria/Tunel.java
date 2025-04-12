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
            ap.moverHumano(0, id, h);
            System.out.println("Humano " + h.getID() + " entra túnel " + id);
            //PRIORIDAD
            synchronized (this) {
                while (!colaTunel.isEmpty()) {
                    System.out.println(h.getID() + " espera porque hay prioridad");
                    wait();
                }
            }
            b.await(); //espera a que haya 3 humanos
            semaforoTunel.acquire();

            System.out.println(h.getID() + " atraviesa túnel" + id);
            Thread.sleep(1000);
            semaforoTunel.release();

            ap.moverHumano(id, 7 + id - 3,h);
            System.out.println("Humano " + h.getID() + " entra en la zona de riesgo " + id);
        } catch (Exception e) {
            System.out.println("Error en túnel " + e.getMessage());
        }
    }

    public void irRefugio(Humano h){
        try {
            System.out.println(h.getID() + " va a la salida");
            colaTunel.put(h); //se mete en la cola
            ap.moverHumano(7 + id - 3, id, h);

            semaforoTunel.acquire(); //de uno en uno
            colaTunel.remove();

            System.out.println(h.getID() + " entra en túnel de vuelta");
            Thread.sleep(1000);
            semaforoTunel.release();

            ap.moverHumano(id, 1, h);

            synchronized (this) {
                notifyAll();
            }
        } catch (Exception e) {
            System.out.println("Error en túnel " + e);
        }
    }
}
