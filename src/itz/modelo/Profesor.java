package itz.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Profesor implements Serializable {

    //Declaracion de variables
    private String nombre, correo, password;
    private ArrayList<Materia> materias;

    //Constructor
    public Profesor(String nombre, String correo, String password) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.materias = new ArrayList<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    // Setters 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}//Fin de clase 
