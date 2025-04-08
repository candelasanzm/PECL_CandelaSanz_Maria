package org.example.pecl_candelasanz_maria;

public class Zombie extends Thread{
    private Apocalipsis apocalipsis;
    private String id;
    private static int contador = 0;

    public Zombie(Apocalipsis apocalipsis) {
        this.apocalipsis = apocalipsis;
        this.id = String.format("Z%04d",contador);
        contador++;
    }
}
