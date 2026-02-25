package itz.controlador;

import itz.modelo.Alumno;

import java.io.*;
import java.util.ArrayList;

public class ControladorProfesor {

    private final String ARCHIVO_CALIF = "calificaciones.dat";
    private ArrayList<String> listaCalificaciones;

    public ControladorProfesor() {
        listaCalificaciones = cargarCalificaciones();
    }

    public void registrarCalificacion(String noControl,
                                       String materia,
                                       double calificacion) {

        String registro = noControl + "," + materia + "," + calificacion;
        listaCalificaciones.add(registro);
        guardarCalificaciones();
    }

    public ArrayList<String> obtenerCalificacionesAlumno(String noControl) {
        ArrayList<String> resultado = new ArrayList<>();

        for (String registro : listaCalificaciones) {
            if (registro.startsWith(noControl + ",")) {
                resultado.add(registro);
            }
        }

        return resultado;
    }

    private void guardarCalificaciones() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(
                             new FileOutputStream(ARCHIVO_CALIF))) {

            oos.writeObject(listaCalificaciones);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> cargarCalificaciones() {
        File archivo = new File(ARCHIVO_CALIF);
        if (!archivo.exists()) return new ArrayList<>();

        try (ObjectInputStream ois =
                     new ObjectInputStream(
                             new FileInputStream(ARCHIVO_CALIF))) {

            return (ArrayList<String>) ois.readObject();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}