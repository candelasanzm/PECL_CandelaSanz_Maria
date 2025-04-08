package org.example.pecl_candelasanz_maria;

import javax.swing.*;
import java.util.ArrayList;

public class ListaHilos {
    private ArrayList<Thread> listado;
    private JTextField txt;

    public ArrayList<Thread> getListado()
    {
        return listado;
    }

    public ListaHilos(JTextField txt){
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
        for(int i = 0; i< listado.size(); i++){
            txtLista = txtLista + "-" + listado.get(i).getName();
        }
        txt.setText(txtLista);
    }
}
