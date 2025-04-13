package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ListaHilosZombie {
    private ArrayList<Zombie> listado;
    private TextField txt;

    public ArrayList<Zombie> getListado() {
        return listado;
    }

    public ListaHilosZombie(TextField txt){
        this.txt = txt;
        listado = new ArrayList<>();
    }

    public synchronized void meterLista(Zombie z){
        listado.add(z);
        imprimirLista();  //cada vez que actualizo imprimo el resultado
    }

    public synchronized void sacarLista(Zombie z){
        listado.remove(z);
        imprimirLista();
    }

    public void imprimirLista(){
        String txtLista = "";
        for(int i = 0; i < listado.size(); i++){
            Zombie z = listado.get(i); //obtiene el hilo
            txtLista = txtLista + "-" + z.getID();
        }
        final String textoImprimir = txtLista.toString();

        Platform.runLater(() -> {txt.setText(textoImprimir); //muestra en la interfaz
        });
    }
}
