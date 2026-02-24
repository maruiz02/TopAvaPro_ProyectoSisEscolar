package itz.controlador;

import itz.modelo.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class ControladorAlumno extends JFrame {
    private JScrollPane scrollAlumnos;
    private DefaultTableModel modelo;
    private JTable tablaAlumnos;
    private ArrayList<Alumno> listaEstudiantes = new ArrayList<>();
    private final String nombreArchivo = "estudiantes.dat";

    public ControladorAlumno() {
        cargarDatos();
        setTitle("Gestión de Alumnos");
        setSize(600, 700);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- TABLA ---
        String[] columnas = {"Matrícula", "Nombre", "Correo"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaAlumnos = new JTable(modelo);
        scrollAlumnos = new JScrollPane(tablaAlumnos);
        scrollAlumnos.setBounds(50, 450, 500, 150);
        add(scrollAlumnos);

        // --- FORMULARIO ---
        JLabel lblNom = new JLabel("Nombre:");
        lblNom.setBounds(50, 50, 100, 30);
        add(lblNom);
        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(50, 80, 200, 30);
        add(txtNombre);

        JLabel lblMat = new JLabel("Matrícula:");
        lblMat.setBounds(50, 120, 100, 30);
        add(lblMat);
        JTextField txtMatricula = new JTextField();
        txtMatricula.setBounds(50, 150, 200, 30);
        add(txtMatricula);

        JButton btnGuardar = new JButton("Registrar y Guardar");
        btnGuardar.setBounds(50, 210, 200, 40);
        add(btnGuardar);

        // Lógica de guardado
        btnGuardar.addActionListener(e -> {
            String n = txtNombre.getText();
            String m = txtMatricula.getText();
            if(!n.isEmpty() && !m.isEmpty()) {
                Alumno nuevo = new Alumno(n, m, n.toLowerCase() + "@escuela.com");
                listaEstudiantes.add(nuevo);
                guardarEnArchivo();
                actualizarTabla();
                txtNombre.setText("");
                txtMatricula.setText("");
            }
        });

        actualizarTabla(); // Mostrar datos al abrir
    }//Fin del constructor
    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Alumno a : listaEstudiantes) {
            modelo.addRow(new Object[]{a.getMatricula(), a.getNombre(), a.getCorreo()});
        }
    }

    private void guardarEnArchivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(listaEstudiantes);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarDatos() {
        File file = new File(nombreArchivo);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
                listaEstudiantes = (ArrayList<Alumno>) ois.readObject();
            } catch (Exception e) { listaEstudiantes = new ArrayList<>(); }
        }
    }
    
}//Fin de la clase
