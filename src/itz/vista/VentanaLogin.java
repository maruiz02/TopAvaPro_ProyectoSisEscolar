package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JButton btnLogin;

    // Colores del tema
    private final Color AZUL_MODERNO = new Color(63, 81, 181);
    private final Color AZUL_HOVER = new Color(48, 63, 159);
    private final Color FONDO_GRIS = new Color(240, 242, 245);

    public VentanaLogin() {
        setTitle("Acceso al Sistema - ITZ");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel Principal con fondo gris claro
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(FONDO_GRIS);
        
        // Tarjeta Blanca Central
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 40, 40)
        ));

        // COMPONENTES 
        
        // Icono o Título
        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(AZUL_MODERNO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo Usuario
        JLabel lblUser = new JLabel("Usuario / Matrícula");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsuario = crearTextFieldPersonalizado();

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtContra = new JPasswordField();
        estilizarCampo(txtContra);

        // Botón Login
        btnLogin = new JButton("Iniciar Sesión");
        estilizarBoton(btnLogin);

        //  ARMADO DE LA TARJETA
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(lblUser);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtUsuario);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(lblPass);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(txtContra);
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        card.add(btnLogin);

        // Agregar tarjeta al centro
        panelPrincipal.add(card);
        add(panelPrincipal);
    }

    // MÉTODOS DE ESTILO 

    private JTextField crearTextFieldPersonalizado() {
        JTextField tf = new JTextField();
        estilizarCampo(tf);
        return tf;
    }

    private void estilizarCampo(JTextField field) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void estilizarBoton(JButton btn) {
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_MODERNO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(AZUL_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(AZUL_MODERNO);
            }
        });
    }

    // Getters necesarios para el controlador
    public JTextField getTxtUsuario() { return txtUsuario; }
    public JPasswordField getTxtContra() { return txtContra; }
    public JButton getBtnLogin() { return btnLogin; }
}