package itz.vista;

import javax.swing.*;

public class VentanaHorarios extends JFrame {

    public VentanaHorarios() {

        setTitle("Horario");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Aquí irá la gestión de horarios");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        add(lbl);
        setVisible(true);
    }
}