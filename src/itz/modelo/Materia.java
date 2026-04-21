package itz.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Materia implements Serializable {

    //Declaracion de varaibles
    private String nombre, clave;
    private ArrayList<Alumno> alumnos;
    private Horario horario;

    //Constructor
    public Materia(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
        this.alumnos = new ArrayList<>();
    }

    //Getters
    public Horario getHorario() {
        return horario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    //Setters
    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}//Fin de la clase
