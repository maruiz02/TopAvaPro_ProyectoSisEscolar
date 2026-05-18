package itz.modelo;

import java.util.ArrayList;

public class Alumno extends Usuario {

    //Declaracion de variables 
    private static final long serialVersionUID = 2L;
    private String matricula;
    private ArrayList<Materia> materias;
    private Kardex kardex;
    private boolean inscripcionPermitida = false;
    
    //Constructor
    public Alumno(String nombre, String correo, String password, String matricula) {
        super(0, nombre, correo, password);   // id no aplica para Alumno solo la matricula 
        this.matricula = matricula;
        this.materias = new ArrayList<>();
        this.kardex = new Kardex();
        this.inscripcionPermitida = false;
    }

    //Getters 
    public String getMatricula() {
        return matricula;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public Kardex getKardex() {
        return kardex;
    }

    public boolean isInscripcionPermitida() {
        return inscripcionPermitida;
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

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setInscripcionPermitida(boolean inscripcionPermitida) {
        this.inscripcionPermitida = inscripcionPermitida;
    }
}
