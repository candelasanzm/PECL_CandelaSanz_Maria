package org.example.pecl_candelasanz_maria;

public class Humano extends Thread {
    private Apocalipsis ap;
    private String id;
    private static int contador = 0;
    private Zona zona;

    public Humano(Apocalipsis ap, Zona zona){
        contador++;
        this.id = String.format("H%04d", contador);
        this.ap = ap;
        this.zona = zona;
    }

    public String getID(){
        return id;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void run(){
        try{
           while(true) {
               // Se crea el humano
               System.out.println("Nuevo humano " + id);

               // El humano empieza en la zona común
               System.out.println("Humano " + id + " está en la zona común");
               ap.moverHumano(ap.getZonas(0), this);
               sleep((int) (Math.random() * 1000) + 1000);

               // El humano sale de la zona común por un túnel elegido de forma aleatoria
               int tunelID = 3 + (int) (Math.random() * 4); // sumo 3 porque los ids de los túneles son 3, 4, 5 y 6
               Tunel tunel = ap.getTunel(tunelID - 3);
               System.out.println("Humano " + id + " se mueve al tunel " + tunelID);
               tunel.salirExterior(this);

               // El humano está en la zona de riesgo
               /*
               sleep((int) (Math.random() * 3000) + 2000);
               System.out.println("Humano " + id + " recolecta 2 piezas de comida");

               // Hacer lo del ataque

               // El humano vuelve a la zona Común
               System.out.println("Humano " + id + " vuelve al refugio");
               //tunel.irRefugio(this);

               // Dejar comida
               ap.dejarComida(this, 2);

               // Va a la zona de descanso
               System.out.println("Humano " + id + " entra en la zona de descanso");
               ap.moverHumano(ap.getZonas(1), this);
               sleep((int) (Math.random() * 2000) + 2000); //duerme en la zona de descanso

               // Comer en el comedor
               System.out.println("Humano " + id + " entra al comedor");
               ap.moverHumano(ap.getZonas(2),this);

               // Come
               ap.cogerComida(this);
               sleep((int) (Math.random() * 3000) + 2000);
                */
               //Si le atacan vuelve a la zona de descanso
               /*
               if(atacado){
                   ap.meterZonaDescansoAtaque(this);
                   sleep((int) (Math.random() * 2000) + 3000);
                   ap.salirZonaDescansoAtaque(this);
               }else{ //si no vuelve a la zona comun
                   ap.salirComedor(this);
               }
               */

           }
        } catch(Exception e){
            System.out.println("Error en humano" + e);
        }
    }

}