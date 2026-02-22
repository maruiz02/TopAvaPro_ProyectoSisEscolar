package itz.vista;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author marco
 */
public class VentanaAlumno extends JFrame {
    //Declaraciones
    JFrame ventanaA = new JFrame("Alumno");
    JLabel lblBienvenida = new JLabel();
    JLabel lblMaterias = new JLabel();
    JLabel lblCalificaciones = new JLabel();
    JLabel lblKardex = new JLabel();
    JButton btnMaterias = new JButton("Abrir");
    JButton btnCalificaciones = new JButton("Abrir");
    JButton btnKardex = new JButton("Abrir");

    public VentanaAlumno() {
        //Atributos de la vetana
        ventanaA.setTitle("Panel del Alumno");
        ventanaA.setSize(600, 600);
        ventanaA.setLayout(null);
        ventanaA.setResizable(false);
        ventanaA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaA.setLocationRelativeTo(null);
        ventanaA.setVisible(true);

        //Labels
        lblBienvenida.setText("Bienvenido Alumno(a)");
        lblBienvenida.setBounds(230, 0, 200, 30);
        ventanaA.add(lblBienvenida);
        lblMaterias.setText("Materias");
        lblMaterias.setBounds(50, 80, 200, 30);
        ventanaA.add(lblMaterias);
        lblCalificaciones.setText("Calificaciones");
        lblCalificaciones.setBounds(50, 180, 200, 30);
        ventanaA.add(lblCalificaciones);
        lblKardex.setText("Kardex");
        lblKardex.setBounds(50, 280, 280, 30);
        ventanaA.add(lblKardex);

        //Botones
        btnMaterias.setBounds(50, 100, 200, 40);
        btnCalificaciones.setBounds(50, 200, 200, 40);
        btnKardex.setBounds(50, 300, 200, 40);
        ventanaA.add(btnMaterias);
        ventanaA.add(btnCalificaciones);
        ventanaA.add(btnKardex);
        btnMaterias.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Materias");

        });//Fin del action listener

        btnCalificaciones.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Calificaciones");
        });//Fin del action listener

        btnKardex.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Kardex");
        });//Fin del action listener

        //Foto
        PruebaFoto foto = new PruebaFoto();
        foto.setBounds(320, 90, 210, 210);
        foto.setBackground(Color.WHITE);
        ventanaA.add(foto);
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
