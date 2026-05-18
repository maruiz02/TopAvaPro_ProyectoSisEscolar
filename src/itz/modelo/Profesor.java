package itz.modelo;

import java.util.ArrayList;

public class Profesor extends Usuario {

    //Declaracion de las variables 
    private static final long serialVersionUID = 2L;
    private ArrayList<Materia> materias;

    //Constructor
    public Profesor(String nombre, String correo, String password) {
        super(0, nombre, correo, password);   // id no aplica para Profesor
        this.materias = new ArrayList<>();
    }

    //Getters
    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    //Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
