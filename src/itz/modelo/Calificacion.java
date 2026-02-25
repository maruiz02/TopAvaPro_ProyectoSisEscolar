package itz.modelo;

import java.io.Serializable;

public class Calificacion implements Serializable {

    private String matricula;
    private String materia;
    private double calificacion;

    public Calificacion(String matricula, String materia, double calificacion) {
        this.matricula = matricula;
        this.materia = materia;
        this.calificacion = calificacion;
    }

    public String getMatricula() { return matricula; }
    public String getMateria() { return materia; }
    public double getCalificacion() { return calificacion; }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}