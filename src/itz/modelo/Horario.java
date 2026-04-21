package itz.modelo;

import java.io.Serializable;

public class Horario implements Serializable {

    //Declaracion de variables
    private String dia;
    private String hora;

    //Constructor
    public Horario(String dia, String hora) {
        this.dia = dia;
        this.hora = hora;
    }

    //Getters
    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    //Setters
    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}//Fin de la clase
