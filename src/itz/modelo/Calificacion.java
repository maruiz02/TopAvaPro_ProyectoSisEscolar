package itz.modelo;

import java.io.Serializable;

public class Calificacion implements Serializable {
    //Declaracion de variables
    private static final long serialVersionUID = 1L;
    private Materia materia;
    private double valor;
    
    //Constructor
    public Calificacion(Materia materia, double valor) {
        this.materia = materia;
        this.valor = valor;
    }

    // Getters
    public double getValor() { 
        return valor; 
    }
    public Materia getMateria() { 
        return materia; 
    }

    // Setter
    public void setValor(double valor) { 
        this.valor = valor;
    }
}//Fin de la clase 
