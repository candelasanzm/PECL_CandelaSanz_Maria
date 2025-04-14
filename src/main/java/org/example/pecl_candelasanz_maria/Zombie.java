package org.example.pecl_candelasanz_maria;

public class Zombie extends Thread{
    private Apocalipsis apocalipsis;
    private String id;
    private int zona = -1; //Si es -1 todavia no tiene zona
    private int contadorMuertes = 0;

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
            System.out.println("Se ha creado un nuevo zombie con id " + id);
            while(true){

                // Elige la zona a la que moverse
                int nuevaZona = 15 + (int) (Math.random() * 4); // genera un id entre 15 y 18 ya que las zonas de riesgo son la 15, 16, 17 y 18
                apocalipsis.moverZonaZombie(this, nuevaZona);

                // Intenta atacar si hay humanos, sino cambia de zona
                if (apocalipsis.getListaHumanosEnZona(nuevaZona).getListado().isEmpty()){
                    // si no hay humanos cambia de zona
                    System.out.println("Zombie " + id + " no encuentra humanos en la zona " + nuevaZona);

                } else { // si hay humanos ataca
                    ListaHilosHumano listaHumanosEnZona = apocalipsis.getListaHumanosEnZona(nuevaZona);
                    int numHumanosEnZona = listaHumanosEnZona.getListado().size();

                    System.out.println("Zombie " + id + " encuentra " + numHumanosEnZona + " humanos en la zona " + nuevaZona);
                    apocalipsis.comprobarParaAtacar(this, apocalipsis.getZonas(nuevaZona));
                }
            }
        }catch(Exception e){
            System.out.println("Error en zombie " + e);
        }
    }
}
