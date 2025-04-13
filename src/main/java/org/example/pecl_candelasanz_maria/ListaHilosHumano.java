package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class ListaHilosHumano {
    private ArrayList<Humano> listado;
    private TextField txt;

    public ArrayList<Humano> getListado() {
        return listado;
    }

    public ListaHilosHumano(TextField txt){
        this.txt = txt;
        listado = new ArrayList<>();
    }

    public synchronized void meterLista(Humano h){
        listado.add(h);
        imprimirLista();  //cada vez que actualizo imprimo el resultado
    }

    public synchronized void sacarLista(Humano h){
        listado.remove(h);
        imprimirLista();
    }

    public void imprimirLista(){
        String txtLista = "";
        for(int i = 0; i < listado.size(); i++){
            Humano h = listado.get(i); //obtiene el hilo
            txtLista = txtLista + "-" + h.getID();
        }
        final String textoImprimir = txtLista.toString();

        Platform.runLater(() -> {txt.setText(textoImprimir); //muestra en la interfaz
        });
    }
}
