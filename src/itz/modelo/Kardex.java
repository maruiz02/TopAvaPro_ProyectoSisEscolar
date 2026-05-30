package itz.modelo;

public class Kardex {

    private int id;
    private int alumnoId;
    private double promedio;
    private int creditos;

    public Kardex() {
    }

    public Kardex(int id,
                  int alumnoId,
                  double promedio,
                  int creditos) {

        this.id = id;
        this.alumnoId = alumnoId;
        this.promedio = promedio;
        this.creditos = creditos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}