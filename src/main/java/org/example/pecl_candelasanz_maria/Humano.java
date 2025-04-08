package org.example.pecl_candelasanz_maria;

public class Humano extends Thread {
    private Apocalipsis ap;
    private String id;
    private static int contador = 0;

    public Humano(Apocalipsis ap){
        contador++;
        this.id = String.format("H%04d",contador);
        this.ap = ap;
    }

    public void run(){
        try{
            ap.entrarZonaComun(this);
            sleep((int) Math.random() * 1000 + 1000);
            ap.salirZonaComun(this);
        } catch(Exception e){
            System.out.println("Error en humano"+e);
        }
    }
}