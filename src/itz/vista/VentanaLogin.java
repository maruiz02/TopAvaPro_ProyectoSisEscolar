package itz.vista;

import itz.util.NavegacionTeclado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblIconoLogin;

    //Constructor 
    public VentanaLogin() {
        setTitle("Acceso al Sistema ITZ");
        setSize(350, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        itz.App.cambiarIcono(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        panel.setBackground(Color.WHITE);

        lblIconoLogin = new JLabel();
        lblIconoLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIconoLogin.setOpaque(false);

        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/resources/login.png"));
            Image imgRedimensionada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblIconoLogin.setIcon(new ImageIcon(imgRedimensionada));
        } catch (Exception e) {
            lblIconoLogin.setText("Logo no disponible");
            System.err.println("No se encontro /resources/login.png");
        }//Fin try-catch

        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        estilizarCampo(txtCorreo);
        estilizarCampo(txtPassword);

        NavegacionTeclado.registrar(txtCorreo, txtPassword);

        btnLogin = new JButton("ENTRAR");
        estilizarBoton(btnLogin);

        panel.add(lblIconoLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtCorreo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JLabel("Contrasena:"));
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        txtCorreo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocusInWindow();
                }
            }
        });

        getRootPane().setDefaultButton(btnLogin);
    }

    //API publica para el controlador
    public String getUsuario() {
        return txtCorreo.getText().trim();
    }

    public String getPasswordTexto() {
        return new String(txtPassword.getPassword());
    }

    public void addLoginListener(ActionListener l) {
        btnLogin.addActionListener(l);
    }

    public void setBloqueado(boolean bloqueado) {
        btnLogin.setEnabled(!bloqueado);
        txtCorreo.setEnabled(!bloqueado);
        txtPassword.setEnabled(!bloqueado);
        if (bloqueado) {
            btnLogin.setText("BLOQUEADO");
            btnLogin.setBackground(new Color(192, 57, 43));
        }//Fin if 
    }

    //Estilo privado 
    private void estilizarCampo(JTextField campo) {
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

    // Getters heredados del codigo original (los usa ControladorLogin internamente)
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }
}//Fin de la clase 
