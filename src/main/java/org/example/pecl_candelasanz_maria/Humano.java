package org.example.pecl_candelasanz_maria;

public class Humano extends Thread {
    private Apocalipsis apocalipsis;
    private String id;
    private static int contador = 0;
    private Zona zona;
    private ApocalipsisLogs apocalipsisLogs = ApocalipsisLogs.getInstancia();

    // Variables para ver si el humano está vivo o marcado
    private boolean vivo = true;
    private boolean marcado = false;

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

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo){
        this.vivo = vivo;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public void run(){
        try{
           // Se crea el humano
           apocalipsisLogs.registrarEvento("Nuevo humano " + id);
           apocalipsisLogs.registrarEvento("Humano " + id + " está en la Zona Común");

           while(true) {
               // El humano empieza en la zona común
               apocalipsis.moverHumano(apocalipsis.getZonas(0),this);
               sleep((int) (Math.random() * 1000) + 1000);

               // El humano sale de la zona común por un túnel elegido de forma aleatoria
               int tunelID = 3 + (int) (Math.random() * 4); // sumo 3 porque los ids de los túneles son 3, 4, 5 y 6
               Tunel tunel = apocalipsis.getTunel(tunelID - 3);
               tunel.salirExterior(this);

               // El humano está en la zona de riesgo
               sleep((int) (Math.random() * 3000) + 2000);
               if (! isVivo()) {
                   apocalipsisLogs.registrarEvento("Humano " + id + " no pudo defenderse y muere");
                   break;

               } else if (isMarcado()) {
                   //Si le atacan vuelve directamente a la zona de descanso
                   apocalipsis.moverHumano(apocalipsis.getZonas(1), this); // si el humano se consigue defender pasa a la zona de descanso sin recoger la comida
                   apocalipsisLogs.registrarEvento("Humano " + id + " está marcado y regresa a la zona segura sin recolectar comida");
                   sleep((int) (Math.random() * 2000) + 3000);

               } else { // Si no es atacado
                   // El humano vuelve a la zona segura
                   apocalipsisLogs.registrarEvento("Humano " + id + " recolecta 2 piezas de comida y vuelve al refugio");
                   tunel.irRefugio(this);

                   // Deja comida en el "almacén"
                   apocalipsis.dejarComida(this, 2);

                   // Entra en la zona de descanso
                   sleep((int) (Math.random() * 2000) + 2000); //duerme en la zona de descanso
               }

               // Comer en el comedor
               apocalipsis.moverHumano(apocalipsis.getZonas(2),this);

               // Come
               apocalipsis.cogerComida(this);
               sleep((int) (Math.random() * 3000) + 2000);

               if(isMarcado()){
                   //Vuelve a la zona de descanso
                   apocalipsis.moverHumano(apocalipsis.getZonas(1), this);
                   apocalipsisLogs.registrarEvento("Humano " + id + " está marcado y vuelve a descansar");
                   Thread.sleep((int)(Math.random() * 2) + 3);

                   //Se recupera
                   setMarcado(false);
               }

               apocalipsis.recuentoHumanos(); // vemos cuantos humanos hay en cada zona
           }
        } catch(Exception e){
            apocalipsisLogs.registrarEvento("Error en humano" + e);
        }
    }

}