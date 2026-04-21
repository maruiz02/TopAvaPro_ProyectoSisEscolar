package itz.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Kardex implements Serializable {
    //Declaracion de variables 
    private ArrayList<Calificacion> historial = new ArrayList<>();

    public void agregarCalificacion(Calificacion c) { 
        historial.add(c); 
    }
    public ArrayList<Calificacion> getHistorial() { 
        return historial; 
    }

    public double calcularPromedio() {
        if (historial.isEmpty()) {
            return 0.0;
        }//Fin if 
        double suma = 0;
        for (Calificacion c : historial) suma += c.getValor();
        return suma / historial.size();
    }
}//Fin de la clase

