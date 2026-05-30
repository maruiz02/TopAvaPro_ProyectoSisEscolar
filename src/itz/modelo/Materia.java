package itz.modelo;

public class Materia {

    private String clave;
    private String nombre;
    private String dia;
    private String hora;

    public Materia() {
    }

    public Materia(String clave, String nombre,
                   String dia, String hora) {

        this.clave = clave;
        this.nombre = nombre;
        this.dia = dia;
        this.hora = hora;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
