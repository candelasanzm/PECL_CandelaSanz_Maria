package org.example.pecl_candelasanz_maria;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Semaphore;

public class Tunel {
    private Apocalipsis ap;
    private Semaphore semaforoTunel;
    private int id;
    private CyclicBarrier b;

    public Tunel(int id, Apocalipsis ap){
        this.id=id;
        this.ap=ap;
        this.semaforoTunel = new Semaphore(1); //entran de uno en uno
        this.b = new CyclicBarrier(3); //pasan en grupos de 3
    }

    public void salirExterior(Humano h){
        try {
            ap.meterEntradaTunel(id,h);
            b.await(); //espera a que haya 3 humanos
            semaforoTunel.acquire();
            ap.meterTunel(id,h);
            System.out.println("Atraviesa tunel" + h.getID());
            Thread.sleep(1000);
            semaforoTunel.release();
            ap.meterRiesgoHumanos(id,h);
        }catch(Exception e){
            System.out.println("Error en tunel"+e);
        }
    }
}
