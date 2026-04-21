package itz.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Alumno implements Serializable {

    //Declaracion de variables
    private static final long serialVersionUID = 1L;
    private String nombre, correo, password, matricula;
    private ArrayList<Materia> materias;
    private Kardex kardex;
    private boolean inscripcionPermitida = false;

    //Constructor
    public Alumno(String nombre, String correo, String password, String matricula) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.matricula = matricula;
        this.materias = new ArrayList<>();
        this.kardex = new Kardex();
        this.inscripcionPermitida = false;
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

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setInscripcionPermitida(boolean inscripcionPermitida) {
        this.inscripcionPermitida = inscripcionPermitida;
    }
}//Fin de la clase
