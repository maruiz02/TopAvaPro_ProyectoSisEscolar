package itz.modelo;

import java.io.Serializable;

public abstract class Usuario implements Serializable{
    
    //Declaracion de variables 
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String nombre;
    protected String correo;
    protected String password;

    //Constructor
    public Usuario(int id, String nombre, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }
    
    //Getters
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