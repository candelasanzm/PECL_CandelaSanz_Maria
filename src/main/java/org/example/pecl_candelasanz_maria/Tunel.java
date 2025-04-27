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
            //Mover a la entrada del tunel
            apocalipsis.moverHumano(apocalipsis.getZonas(id), h);

            //PRIORIDAD
            synchronized (this) {
                while (!colaTunel.isEmpty()) {
                    apocalipsisLogs.registrarEvento(h.getID() + " espera porque un grupo previo tiene prioridad");
                    wait();
                }
            }

            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " está esperando para formar grupo en el Túnel " + (id - 2));
            barrier.await(); // Espera a que haya 3 humanos
            apocalipsisLogs.registrarEvento("Grupo formado en Túnel " + (id - 2));

            //Entra al tunel
            semaforoTunel.acquire(); // Acceden al túnel de uno en uno
            int tunelInterior = 7 + (id - 3); // Zonas 7,8,9,10
            apocalipsis.moverHumano(apocalipsis.getZonas(tunelInterior), h);
            apocalipsisLogs.registrarEvento(h.getID() + " atraviesa Túnel " + (id - 2));
            Thread.sleep(1000); // Tiempo de cruce del túnel
            semaforoTunel.release(); // Se libera el acceso al túnel

            // Mover a la salida del Túnel
            int salidaTunel = 11 + (id - 3); // Zonas 11, 12, 13, 14
            apocalipsis.moverHumano(apocalipsis.getZonas(salidaTunel), h);

            // Mover zona riesgo
            int zonaRiesgo = 15 + (id - 3); // Zonas 15,16,17,18
            apocalipsis.moverHumano(apocalipsis.getZonas(zonaRiesgo), h);
            apocalipsisLogs.registrarEvento("Humano " + h.getID() + " entra en la Zona de Riesgo " + (id - 2));

        } catch (Exception e) {
            apocalipsisLogs.registrarEvento("Error en Túnel " + e.getMessage());
        }
    }

    public void irRefugio(Humano h){
        try {
            apocalipsisLogs.registrarEvento(h.getID() + " regresa a la zona segura desde el Túnel " + (id - 2));
            colaTunel.put(h); //se mete en la cola de salida

            // Entra a la "salida" del túnel
            int zonaSalida = 11 + (id - 3); // Zonas 11,12,13,14
            apocalipsis.moverHumano(apocalipsis.getZonas(zonaSalida), h);

            semaforoTunel.acquire(); // Pasan de uno en uno
            colaTunel.remove(); // Cruza el tunel

            // Entra al interior del túnel
            int tunelInterior = 7 + (id - 3); //Zonas 7,8,9,10
            apocalipsis.moverHumano(apocalipsis.getZonas(tunelInterior), h);
            Thread.sleep(1000);
            semaforoTunel.release();

            // Mover Zona descanso
            apocalipsis.moverHumano(apocalipsis.getZonas(1), h);

            synchronized (this) {
                notifyAll();
            }

        } catch (Exception e) {
            apocalipsisLogs.registrarEvento("Error en Túnel " + e);
        }
    }
}
