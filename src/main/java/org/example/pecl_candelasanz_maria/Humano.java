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
            // Se crea el humano
            System.out.println("Nuevo humano " + id);
            // El humano empieza en la zona común
            ap.entrarZonaComun(this);
            sleep((int) (Math.random() * 1000) + 1000);
            // El humano sale de la zona común por un túnel elegido de forma aleatoria
            ap.salirZonaComun(this);
            int tunel = (int) (Math.random() * 4) + 1;
            ap.irTunel(tunel,this);
            // En la zona exterior están un tiempo aleatorio
            sleep((int) (Math.random() * 3000) + 2000);
            //Coge comida
            System.out.println("Humano " + id + " está recolectando comida");
            ap.cogerComida();
            // Hacer lo del ataque
            // El humano vuelve a la zona Común
            ap.irRefugio(tunel, this);
            // Dejar comida
            ap.dejarComida(2);
            System.out.println("Humano " + id + " ha dejado la comida");
            // Entrar en la zona de descanso
            ap.meterZonaDescanso(tunel, this);
            sleep((int) (Math.random() * 2000) + 2000);
            // Entrar al comedor
            ap.meterComedor(this);
        } catch(Exception e){
            System.out.println("Error en humano" + e);
        }
    }

}