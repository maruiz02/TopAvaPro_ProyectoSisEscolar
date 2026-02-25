package itz.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profesor implements Serializable {

    private String nombre;
    private String id;

    private List<Materia> materiasAsignadas;

    public Profesor(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
        this.materiasAsignadas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public List<Materia> getMateriasAsignadas() {
        return materiasAsignadas;
    }

    public void asignarMateria(Materia materia) {
        materiasAsignadas.add(materia);
    }
}