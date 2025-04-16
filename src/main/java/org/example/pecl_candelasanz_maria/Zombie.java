package org.example.pecl_candelasanz_maria;

public class Zombie extends Thread{
    private Apocalipsis apocalipsis;
    private String id;
    private int zona = -1; //Si es -1 todavia no tiene zona
    private int contadorMuertes = 0;
    private ApocalipsisLogs apocalipsisLogs = ApocalipsisLogs.getInstancia();

    public Zombie(Apocalipsis apocalipsis, String id) {
        this.apocalipsis = apocalipsis;
        this.id = id;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getID(){
        return id;
    }

    public void anadirMuerte(){
        contadorMuertes++;
    }

    public int getContadorMuertes(){
        return contadorMuertes;
    }

    public void run(){
        try {
            apocalipsisLogs.registrarEvento("Se ha creado un nuevo zombie con id " + id);
            while(true){

                // Elige la zona a la que moverse
                int nuevaZona = 15 + (int) (Math.random() * 4); // genera un id entre 15 y 18 ya que las zonas de riesgo son la 15, 16, 17 y 18
                apocalipsis.moverZonaZombie(this, nuevaZona);

                // Intenta atacar si hay humanos, sino cambia de zona
                if (apocalipsis.getListaHumanosEnZona(nuevaZona).getListado().isEmpty()){
                    // si no hay humanos cambia de zona
                    apocalipsisLogs.registrarEvento("Zombie " + id + " no encuentra humanos en la Zona de Riesgo " + (nuevaZona - 14));
                    // Espera antes de moverse
                    Thread.sleep((int)(Math.random() * 1000) + 2000);
                } else { // si hay humanos ataca
                    ListaHilosHumano listaHumanosEnZona = apocalipsis.getListaHumanosEnZona(nuevaZona);
                    int numHumanosEnZona = listaHumanosEnZona.getListado().size();

                    apocalipsisLogs.registrarEvento("Zombie " + id + " encuentra " + numHumanosEnZona + " humanos en la Zona de Riesgo " + (nuevaZona - 14));
                    //intenta atacar
                    apocalipsis.comprobarParaAtacar(this, apocalipsis.getZonas(nuevaZona));
                }

                apocalipsis.recuentoZombies(); // vemos cuantos zombies hay en cada zona
            }
        }catch(Exception e){
            apocalipsisLogs.registrarEvento("Error en zombie " + e);
        }
    }
}
