package itz.modelo;

import java.io.*;
import java.util.ArrayList;

public class SistemaEscolar implements Serializable {
    
    //Declaracion de variables
    private static final long serialVersionUID = 1L;
    private ArrayList<Administrador> administradores = new ArrayList<>();
    private ArrayList<Alumno> alumnos = new ArrayList<>();
    private ArrayList<Profesor> profesores = new ArrayList<>();
    private ArrayList<Materia> materias = new ArrayList<>();
    private static final String ARCHIVO = "sistema.dat";

    public ArrayList<Administrador> getAdministradores() {
        return administradores;
    }

    public ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    public ArrayList<Profesor> getProfesores() {
        return profesores;
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }
    
    //Guardando en el archivo
    public void guardarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Leyendo datos del archivo
    public static SistemaEscolar cargarSistema() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (SistemaEscolar) ois.readObject();
        } catch (Exception e) {
            return new SistemaEscolar();
        }
    }
}//Fin de la clase
