package itz.vista;

import itz.controlador.ControladorAlumno;
import itz.modelo.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class VentanaAdmin extends JFrame {
    //Declaraciones
    JFrame ventanaAa = new JFrame("Administrador");
    JLabel lblBienvenida = new JLabel();
    JLabel lblAlumnos = new JLabel();
    JLabel lblProfesores = new JLabel();
    JLabel lblMaterias = new JLabel();
    JButton btnAlumnos = new JButton("Abrir");
    JButton btnProfesores = new JButton("Abrir");
    JButton btnMaterias = new JButton("Abrir");
    private JScrollPane scrollAlumnos;
    private DefaultTableModel modelo;
    private JTable tablaAlumnos;
    private ArrayList<Alumno> listaEstudiantes = new ArrayList<>();
    private final String nombreArchivo = "estudiantes.dat";

    public VentanaAdmin() {
        //Atributos de la vetana
        ventanaAa.setTitle("Panel del Administrador");
        ventanaAa.setSize(600, 600);
        ventanaAa.setLayout(null);
        ventanaAa.setResizable(false);
        ventanaAa.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaAa.setLocationRelativeTo(null);
        ventanaAa.setVisible(true);

        //Labels
        lblBienvenida.setText("Bienvenido Administrador(a)");
        lblBienvenida.setBounds(230, 0, 200, 30);
        ventanaAa.add(lblBienvenida);
        lblAlumnos.setText("Alumnos");
        lblAlumnos.setBounds(50, 80, 200, 30);
        ventanaAa.add(lblAlumnos);
        lblProfesores.setText("Profesores");
        lblProfesores.setBounds(50, 180, 200, 30);
        ventanaAa.add(lblProfesores);
        lblMaterias.setText("Materias");
        lblMaterias.setBounds(50, 280, 280, 30);
        ventanaAa.add(lblMaterias);

        //Botones
        btnAlumnos.setBounds(50, 100, 200, 40);
        btnProfesores.setBounds(50, 200, 200, 40);
        btnMaterias.setBounds(50, 300, 200, 40);
        ventanaAa.add(btnAlumnos);
        ventanaAa.add(btnProfesores);
        ventanaAa.add(btnMaterias);
        btnAlumnos.addActionListener(e -> {
            ControladorAlumno contrAlumno = new ControladorAlumno();
            contrAlumno.setVisible(true);
        });//Fin del action listener

        btnProfesores.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Profesores");
        });//Fin del action listener

        btnMaterias.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Materias");
        });//Fin del action listener

        //Foto
        PruebaFoto foto = new PruebaFoto();
        foto.setBounds(320, 90, 210, 210);
        foto.setBackground(Color.WHITE);
        ventanaAa.add(foto);

    }//Fin del constructor

    class PruebaFoto extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibujar la cabeza
            g.setColor(new Color(255, 206, 158));
            g.fillOval(10, 10, 180, 180);

            // Dibujar los ojos
            g.setColor(Color.BLACK);
            g.fillOval(50, 70, 30, 30);
            g.fillOval(120, 70, 30, 30);

            // Dibujar la boca
            g.setColor(Color.RED);
            g.drawArc(50, 110, 100, 50, 0, -180);

            // Dibujar la nariz
            g.setColor(Color.YELLOW);
            int[] xPoints = {100, 110, 90};
            int[] yPoints = {90, 110, 110};
            g.fillPolygon(xPoints, yPoints, 3);
        }//Fin de la foto
    }//Fin del metodo grafics

}//Fin de la clase
