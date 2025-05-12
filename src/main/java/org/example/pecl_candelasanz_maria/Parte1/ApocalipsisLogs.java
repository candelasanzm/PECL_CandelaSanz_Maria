package org.example.pecl_candelasanz_maria.Parte1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ApocalipsisLogs {
    private static ApocalipsisLogs recursoCompartido;
    private static final String nombreArchivo = "apocalipsis" + new Date().getTime() + ".txt"; // es el nombre del archivo donde se guardarán los log
    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // establezco el formato de fecha y hora que quiero en la entrada de cada log
    private BufferedWriter escribirArchivo;

    public ApocalipsisLogs() {
        try {
            escribirArchivo = new BufferedWriter(new FileWriter(nombreArchivo, true));
        } catch (IOException e) {
            System.out.println("Error al inicializar archivo log: " + e.getMessage());
        }
    }

    // Método para obtener el recurso compartido. Uso sincronizado para proteger el acceso concurrente
    public static synchronized ApocalipsisLogs getInstancia() {
        if (recursoCompartido == null) {
            recursoCompartido = new ApocalipsisLogs();
        }
        return recursoCompartido;
    }

    public synchronized void registrarEvento(String mensaje){
        String entrada = LocalDateTime.now().format(formatoFecha) + " - " + mensaje;

        try {
            escribirArchivo.write(entrada);
            escribirArchivo.newLine(); // Salto de línea
            System.out.println(entrada);
        } catch (IOException e) {
            System.out.println("Error al escribir archivo: " + e.getMessage());
        }
    }
}