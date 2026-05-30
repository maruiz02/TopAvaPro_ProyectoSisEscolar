package itz.modelo;

import java.util.ArrayList;

public class SistemaEscolar {

    private ArrayList<Usuario> usuarios;
    private ArrayList<Materia> materias;
    private ArrayList<Calificacion> calificaciones;
    private ArrayList<Horario> horarios;
    private ArrayList<Kardex> kardex;

    public SistemaEscolar() {
        usuarios = new ArrayList<>();
        materias = new ArrayList<>();
        calificaciones = new ArrayList<>();
        horarios = new ArrayList<>();
        kardex = new ArrayList<>();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(ArrayList<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public ArrayList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public ArrayList<Kardex> getKardex() {
        return kardex;
    }

    public void setKardex(ArrayList<Kardex> kardex) {
        this.kardex = kardex;
    }
}
