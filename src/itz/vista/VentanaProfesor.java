package itz.vista;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author marco
 */
public class VentanaProfesor extends JFrame {
    //Declaraciones
    JFrame ventanaP = new JFrame("Profesor");
    JLabel lblBienvenida = new JLabel();
    JLabel lblAlumnos = new JLabel();
    JLabel lblCalificaciones = new JLabel();
    JLabel lblHorario = new JLabel();
    JButton btnAlumnos = new JButton("Abrir");
    JButton btnCalificaciones = new JButton("Abrir");
    JButton btnHorario = new JButton("Abrir");

    public VentanaProfesor() {
        //Atributos de la vetana
        ventanaP.setTitle("Panel del Profesor");
        ventanaP.setSize(600, 600);
        ventanaP.setLayout(null);
        ventanaP.setResizable(false);
        ventanaP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaP.setLocationRelativeTo(null);
        ventanaP.setVisible(true);

        //Labels
        lblBienvenida.setText("Bienvenido porfesor(a)");
        lblBienvenida.setBounds(230, 0, 200, 30);
        ventanaP.add(lblBienvenida);
        lblAlumnos.setText("Alumnos");
        lblAlumnos.setBounds(50, 80, 200, 30);
        ventanaP.add(lblAlumnos);
        lblCalificaciones.setText("Calificaciones");
        lblCalificaciones.setBounds(50, 180, 200, 30);
        ventanaP.add(lblCalificaciones);
        lblHorario.setText("Horario");
        lblHorario.setBounds(50, 280, 280, 30);
        ventanaP.add(lblHorario);

        //Botones
        btnAlumnos.setBounds(50, 100, 200, 40);
        btnCalificaciones.setBounds(50, 200, 200, 40);
        btnHorario.setBounds(50, 300, 200, 40);
        ventanaP.add(btnAlumnos);
        ventanaP.add(btnCalificaciones);
        ventanaP.add(btnHorario);
        btnAlumnos.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Alumnos");

        });//Fin del action listener

        btnCalificaciones.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Calificaciones");
        });//Fin del action listener

        btnHorario.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Horarios");
        });//Fin del action listener

        //Solo es momentanea la foto luego voy a cambiar el codigo por una galeria a ver si puedo
        PruebaFoto foto = new PruebaFoto();
        foto.setBounds(320, 90, 210, 210);
        foto.setBackground(Color.WHITE);
        ventanaP.add(foto);
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
