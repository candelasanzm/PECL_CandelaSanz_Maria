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
                System.out.println("Nuevo zombie");
                int nuevazona = (int) (Math.random() * 4); //zonas 0,1,2,3
                //Accede zona
                ap.moverZonaZombie(this, nuevazona);
                //ataque
                //permanece en la zona
                sleep((int) (Math.random() * 1000) + 2000);
            }
        }catch(Exception e){
            System.out.println("Error en zombie "+e);
        }
    }
}
