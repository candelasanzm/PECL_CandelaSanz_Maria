package org.example.pecl_candelasanz_maria;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.ArrayList;

public class ListaHilos {
    private ArrayList<Thread> listado;
    private TextField txt;

    public ArrayList<Thread> getListado() {
        return listado;
    }

    public ListaHilos(TextField txt){
        this.txt = txt;
        listado = new ArrayList<>();
    }

    public synchronized void meterLista(Thread t){
        listado.add(t);
        imprimirLista();  //cada vez que actualizo imprimo el resultado
    }

    public synchronized void sacarLista(Thread t){
        listado.remove(t);
        imprimirLista();
    }

    public void imprimirLista(){
        String txtLista = "";
        for(int i = 0; i < listado.size(); i++){
            Humano h = (Humano) listado.get(i); //obtiene el hilo
            txtLista = txtLista + "-" + h.getID();
        }
        final String textoImprimir = txtLista.toString();

        Platform.runLater(() -> {txt.setText(textoImprimir);
        });
    }
}
