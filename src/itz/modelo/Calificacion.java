package itz.modelo;

public class Calificacion {

    private int id;
    private int alumnoId;
    private int materiaId;
    private double calificacion;
    private String periodo;

    public Calificacion() {
    }

    public Calificacion(int id, int alumnoId,
            int materiaId,
            double calificacion,
            String periodo) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.materiaId = materiaId;
        this.calificacion = calificacion;
        this.periodo = periodo;
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

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}