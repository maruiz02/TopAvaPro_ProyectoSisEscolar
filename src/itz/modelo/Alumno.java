package itz.modelo;

import java.io.Serializable;
import java.util.*;

public class Alumno implements Serializable {

    private String nombre;
    private String matricula;
    private String correo;

    private List<Materia> materiasInscritas;
    private Map<String, Double> historialCalificaciones;

    public Alumno(String nombre, String matricula, String correo) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.correo = correo;
        this.materiasInscritas = new ArrayList<>();
        this.historialCalificaciones = new HashMap<>();
    }

    public String getNombre() { return nombre; }
    public String getMatricula() { return matricula; }
    public String getCorreo() { return correo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setCorreo(String correo) { this.correo = correo; }

    public List<Materia> getMateriasInscritas() {
        return materiasInscritas;
    }

    public Map<String, Double> getHistorialCalificaciones() {
        return historialCalificaciones;
    }

    public void agregarMateria(Materia materia) {
        materiasInscritas.add(materia);
    }

    public void registrarCalificacion(String nombreMateria, double calificacion) {
        historialCalificaciones.put(nombreMateria, calificacion);
    }

    public boolean puedeInscribirse(String nombreMateria) {

        if (historialCalificaciones.containsKey(nombreMateria)) {

            double calificacion = historialCalificaciones.get(nombreMateria);

            if (calificacion >= 70) {
                return false;
            }
        }

        return true;
    }
}