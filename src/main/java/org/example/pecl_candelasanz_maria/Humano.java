package org.example.pecl_candelasanz_maria;

public class Humano extends Thread {
    private Apocalipsis apocalipsis;
    private String id;
    private static int contador = 0;
    private Zona zona;

    public Humano(Apocalipsis apocalipsis, Zona zona){
        contador++;
        this.id = String.format("H%04d", contador);
        this.apocalipsis = apocalipsis;
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
           // Se crea el humano
           System.out.println("Nuevo humano " + id);

           while(true) {
               // El humano empieza en la zona común
               apocalipsis.moverHumano(apocalipsis.getZonas(0),this);
               System.out.println("Humano " + id + " está en la Zona Común");
               sleep((int) (Math.random() * 1000) + 1000);

               // El humano sale de la zona común por un túnel elegido de forma aleatoria
               int tunelID = 3 + (int) (Math.random() * 4); // sumo 3 porque los ids de los túneles son 3, 4, 5 y 6
               Tunel tunel = apocalipsis.getTunel(tunelID - 3);
               System.out.println("Humano " + id + " está en la Entrada del Túnel " + tunelID);
               tunel.salirExterior(this);

               // El humano está en la zona de riesgo
               sleep((int) (Math.random() * 3000) + 2000);
               if (! apocalipsis.isVivo()) {
                   System.out.println("Humano " + id + " no pudo defenderse y muere");
                   break;

               } else if (apocalipsis.isDefendido()) {
                   //Si le atacan vuelve directamente a la zona de descanso
                   apocalipsis.moverHumano(apocalipsis.getZonas(1), this); // si el humano se consigue defender pasa a la zona de descanso sin recoger la comida
                   System.out.println("Humano " + id + " está marcado y regresa a la zona segura sin recolectar comida");
                   sleep((int) (Math.random() * 2000) + 3000);

               } else { // Si no es atacado
                   // El humano vuelve a la zona segura
                   System.out.println("Humano " + id + " recolecta 2 piezas de comida y vuelve al refugio");
                   tunel.irRefugio(this);

                   // Deja comida en el "almacén"
                   apocalipsis.dejarComida(this, 2);

                   // Entra en la zona de descanso
                   System.out.println("Humano " + id + " entra en la Zona de Descanso");
                   sleep((int) (Math.random() * 2000) + 2000); //duerme en la zona de descanso
               }

               // Comer en el comedor
               System.out.println("Humano " + id + " entra al Comedor");
               apocalipsis.moverHumano(apocalipsis.getZonas(2),this);

               // Come
               apocalipsis.cogerComida(this);
               sleep((int) (Math.random() * 3000) + 2000);

           }
        } catch(Exception e){
            System.out.println("Error en humano" + e);
        }
    }

}