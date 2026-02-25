package itz.vista;

import itz.controlador.ControladorProfesor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaAlumno extends JFrame {

    private String noControl;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ControladorProfesor controlador;

    public VentanaAlumno(String noControl) {

        this.noControl = noControl;
        controlador = new ControladorProfesor();

        setTitle("Panel Alumno");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modelo = new DefaultTableModel();
        modelo.addColumn("Materia");
        modelo.addColumn("Calificación");

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarCalificaciones();
    }

    private void cargarCalificaciones() {

        modelo.setRowCount(0);

        ArrayList<String> lista =
                controlador.obtenerCalificacionesAlumno(noControl);

        for (String registro : lista) {
            String[] partes = registro.split(",");
            modelo.addRow(new Object[]{
                    partes[1],
                    partes[2]
            });
        }
    }
}