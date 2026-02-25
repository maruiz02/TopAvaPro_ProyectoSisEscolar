package itz.controlador;

import itz.modelo.Alumno;

import java.io.*;
import java.util.ArrayList;

public class ControladorAlumno {

    private ArrayList<Alumno> listaAlumnos;
    private final String ARCHIVO = "estudiantes.dat";

    public ControladorAlumno() {
        listaAlumnos = new ArrayList<>();
        cargarAlumnos();
    }

    public void registrarAlumno(Alumno alumno) {
        listaAlumnos.add(alumno);
        guardarAlumnos();
    }

    public void actualizarAlumno(Alumno alumnoActualizado) {

        for (int i = 0; i < listaAlumnos.size(); i++) {

            if (listaAlumnos.get(i).getMatricula()
                    .equalsIgnoreCase(alumnoActualizado.getMatricula())) {

                listaAlumnos.set(i, alumnoActualizado);
                break;
            }
        }

        guardarAlumnos();
    }

    public void eliminarAlumno(String matricula) {

        listaAlumnos.removeIf(a ->
                a.getMatricula().equalsIgnoreCase(matricula));

        guardarAlumnos();
    }

    public ArrayList<Alumno> obtenerAlumnos() {
        return listaAlumnos;
    }

    private void guardarAlumnos() {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(
                             new FileOutputStream(ARCHIVO))) {

            oos.writeObject(listaAlumnos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarAlumnos() {

        File file = new File(ARCHIVO);
        if (!file.exists()) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(
                             new FileInputStream(ARCHIVO))) {

            listaAlumnos =
                    (ArrayList<Alumno>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}