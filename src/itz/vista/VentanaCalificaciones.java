package itz.vista;

import javax.swing.*;

public class VentanaCalificaciones extends JFrame {

    public VentanaCalificaciones() {

        setTitle("Calificaciones");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Aquí irá el CRUD de Calificaciones");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        add(lbl);
        setVisible(true);
    }
}