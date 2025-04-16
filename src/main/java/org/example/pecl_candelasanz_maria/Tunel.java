package org.example.pecl_candelasanz_maria;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Tunel {
    private Apocalipsis apocalipsis;
    private Semaphore semaforoTunel; // controla que el acceso al tunel sea de uno en uno
    private int id; // identificador del tunel
    private CyclicBarrier barrier; // permite que los humanos puedan pasar en grupos
    private final LinkedBlockingQueue colaTunel = new LinkedBlockingQueue<>(); // sin limite
    private ApocalipsisLogs apocalipsisLogs = ApocalipsisLogs.getInstancia();

    public Tunel(int id, Apocalipsis apocalipsis){
        this.id = id;
        this.apocalipsis = apocalipsis;
        this.semaforoTunel = new Semaphore(1); // entran de uno en uno
        this.barrier = new CyclicBarrier(3); // pasan en grupos de 3
    }

    public void salirExterior(Humano h){
        try {
            // Humano entra en túnel
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " intenta entrar al Túnel " + (id-2));
            //Mover a la entrada del tunel
            apocalipsis.moverHumano(apocalipsis.getZonas(id), h);

            //PRIORIDAD
            synchronized (this) {
                while (!colaTunel.isEmpty()) {
                    apocalipsisLogs.registrarEvento(h.getID() + " espera porque hay prioridad");
                    wait();
                }
            }

            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " está esperando para formar grupo en el Túnel " + (id-2));
            barrier.await(); //espera a que haya 3 humanos
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " forma grupo en el Túnel " + (id-2));

            //Entra al tunel
            semaforoTunel.acquire(); // acceden al túnel de uno en uno
            int tunelInterior = 7 + (id - 3); //Zonas 7,8,9,10
            apocalipsis.moverHumano(apocalipsis.getZonas(tunelInterior), h);
            apocalipsisLogs.registrarEvento(h.getID() + " atraviesa Túnel" + (id-2));
            Thread.sleep(1000); // tiempo de cruce
            semaforoTunel.release(); // se libera el acceso al túnel

            //mover zona riesgo
            int zonaRiesgo = 15 + (id - 3); //Zonas 15,16,17,18
            apocalipsis.moverHumano(apocalipsis.getZonas(zonaRiesgo), h);
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " entra en la Zona de Riesgo " + (id-2));

        } catch (Exception e) {
            apocalipsisLogs.registrarEvento("Error en Túnel " + e.getMessage());
        }
    }

    public void irRefugio(Humano h){
        try {
            apocalipsisLogs.registrarEvento(h.getID() + " regresa a la zona segura desde el Túnel " + id);
            colaTunel.put(h); //se mete en la cola de salida

            //entra a la  salida
            int zonaSalida = 11 + (id - 3); //zonas 11,12,13,14
            apocalipsis.moverHumano(apocalipsis.getZonas(zonaSalida), h);

            semaforoTunel.acquire(); //de uno en uno
            colaTunel.remove(); //cruza el tunel

            //entra al interior del tunel
            int tunelInterior = 7 + (id - 3); //Zonas 7,8,9,10
            apocalipsis.moverHumano(apocalipsis.getZonas(tunelInterior), h);
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " entra en Túnel de vuelta");
            Thread.sleep(1000);
            semaforoTunel.release();

            //Mover Zona descanso
            apocalipsis.moverHumano(apocalipsis.getZonas(1), h);

            synchronized (this) {
                notifyAll();
            }

        } catch (Exception e) {
            apocalipsisLogs.registrarEvento("Error en Túnel " + e);
        }
    }
}
