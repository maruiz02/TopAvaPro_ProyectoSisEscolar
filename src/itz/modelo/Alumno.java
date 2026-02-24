package itz.modelo;

import java.io.Serializable;

/**
 *
 * @author marco
 */
public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String matricula;
    private String correo;

    public Alumno(String nombre, String matricula, String correo) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.correo = correo;
    }//Fin del constructor

    public String getNombre() { return nombre; }
    public String getMatricula() { return matricula; }
    public String getCorreo() { return correo; }
}//Fin de la clase
