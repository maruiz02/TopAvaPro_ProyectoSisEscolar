package itz.modelo;

import java.io.Serializable;

public class Administrador implements Serializable {

    //Declaracion de variables
    private int id;
    private String nombre;
    private String correo;
    private String password;

    //Constructor
    public Administrador(int id, String nombre, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    // Getters
    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }
}//Fin de la clase
