package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaLogin extends JFrame {
    //Declaracion de variables
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    public JButton btnLogin;

    //Constructor
    public VentanaLogin() {
        setTitle("Acceso al Sistema ITZ");
        setSize(350, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Panel Principal con margen
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        // Logo o Título
        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Campos
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        estilizarCampo(txtCorreo, "Correo o Matrícula");
        estilizarCampo(txtPassword, "Contraseña");

        btnLogin = new JButton("ENTRAR");
        estilizarBoton(btnLogin);

        // Agregar componentes
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtCorreo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);
    }

    private void estilizarCampo(JTextField campo, String placeholder) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
    }

    private void estilizarBoton(JButton boton) {
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.setBackground(new Color(52, 152, 219));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    //Getters
    public JTextField getTxtCorreo() {
        return txtCorreo; 
    }
    
    public JPasswordField getTxtPassword() { 
        return txtPassword; 
    }
}//Fin de la clase 