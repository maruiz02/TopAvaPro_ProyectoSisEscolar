package itz.modelo;

import java.io.Serializable;

public class Materia implements Serializable {

    private String nombre;
    private String horario;   
    private String profesor;  

    public Materia(String nombre, String horario, String profesor) {
        this.nombre = nombre;
        this.horario = horario;
        this.profesor = profesor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHorario() {
        return horario;
    }

    public String getProfesor() {
        return profesor;
    }

    @Override
    public String toString() {
        return nombre + " - " + horario + " (" + profesor + ")";
    }
}