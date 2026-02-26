package itz.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaProfesor extends JFrame {

    public JComboBox<String> comboMaterias;
    public JTable tablaAlumnos;
    public JTextField txtCalificacion;
    public JButton btnGuardarCalificacion;

    public VentanaProfesor() {

        setTitle("Panel Profesor");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Materias
        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Materia:"));

        comboMaterias = new JComboBox<>();
        panelSuperior.add(comboMaterias);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla alumnos
        tablaAlumnos = new JTable();
        add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);

        // Calificaciones
        JPanel panelInferior = new JPanel();
        panelInferior.add(new JLabel("Calificación:"));

        txtCalificacion = new JTextField(5);
        panelInferior.add(txtCalificacion);

        btnGuardarCalificacion = new JButton("Guardar");
        panelInferior.add(btnGuardarCalificacion);

        add(panelInferior, BorderLayout.SOUTH);
    }
}