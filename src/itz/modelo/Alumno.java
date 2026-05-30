package itz.modelo;

public class Alumno extends Usuario {

    private String matricula;
    private String carrera;
    private int semestre;

    // NUEVO
    private boolean inscripcionPermitida;

    public Alumno() {
    }

    public Alumno(int id,
                   String nombre,
                   String apellido,
                   String correo,
                   String password,
                   String matricula,
                   String carrera,
                   int semestre,
                   boolean inscripcionPermitida) {

        super(id, nombre, apellido,
              correo, password,
              "ALUMNO");

        this.matricula = matricula;
        this.carrera = carrera;
        this.semestre = semestre;
        this.inscripcionPermitida = inscripcionPermitida;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    // NUEVOS MÉTODOS
    public boolean isInscripcionPermitida() {
        return inscripcionPermitida;
    }

    public void setInscripcionPermitida(boolean inscripcionPermitida) {
        this.inscripcionPermitida = inscripcionPermitida;
    }
}