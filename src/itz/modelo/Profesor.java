package itz.modelo;

public class Profesor extends Usuario {

    private String especialidad;
    private String cedulaProfesional;

    public Profesor() {
    }

    public Profesor(int id,
                    String nombre,
                    String apellido,
                    String correo,
                    String password,
                    String especialidad,
                    String cedulaProfesional) {

        super(id, nombre, apellido,
              correo, password,
              "PROFESOR");

        this.especialidad = especialidad;
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }
}