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

    public String getID(){
        return id;
    }

    public void run(){
        try{
            System.out.println("nuevo humano "+ id);
            ap.entrarZonaComun(this);
            sleep((int) (Math.random() * 1000) + 1000);
            ap.salirZonaComun(this);
            int tunel = (int) (Math.random() * 4) + 1; //elige entre 4 tuneles
            ap.irTunel(tunel,this);
        } catch(Exception e){
            System.out.println("Error en humano" + e);
        }
    }
}