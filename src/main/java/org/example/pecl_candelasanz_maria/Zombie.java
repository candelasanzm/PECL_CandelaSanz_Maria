package org.example.pecl_candelasanz_maria;

public class Zombie extends Thread{
    private Apocalipsis ap;
    private String id;
    private static int contador = 0;
    private int zona = -1; //Si es -1 todavia no tiene zona

    public Zombie(Apocalipsis ap) {
        this.ap = ap;
        this.id = String.format("Z%04d",contador);
        contador++;
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

    public void run(){
        try {
            while(true){
                System.out.println("Se ha creado un nuevo zombie con id " + id);
                int nuevaZona = 15 + (int) (Math.random() * 4); // genera un id entre 15 y 18 ya que las zonas de riesgo son la 15, 16, 17 y 18
                // Accede zona
                ap.moverZonaZombie(this, nuevaZona);
                // Ataque
                // Permanece en la zona
                sleep((int) (Math.random() * 1000) + 2000);
            }
        }catch(Exception e){
            System.out.println("Error en zombie " + e);
        }
    }
}
