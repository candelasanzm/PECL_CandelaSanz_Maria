package org.example.pecl_candelasanz_maria;

public class Zona {
    private int idZona;
    private String nombre;
    private int humano = 0;

    public Zona(int idZona, String nombre) {
        this.idZona = idZona;
        this.nombre = nombre;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
