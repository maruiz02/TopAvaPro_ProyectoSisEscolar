package itz.vista;

import itz.util.NavegacionTeclado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    public JButton btnLogin;
    private JLabel lblIconoLogin; // Componente para el logo del login

    public VentanaLogin() {
        // Configuración básica
        setTitle("Acceso al Sistema ITZ");
        setSize(350, 520); // Ajustado para dar espacio a la imagen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // LLAMADA CENTRALIZADA: Quita a Duke de esta ventana
        itz.App.cambiarIcono(this);

        // Panel Principal con BoxLayout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));
        panel.setBackground(Color.WHITE);

        // --- SECCIÓN DEL ICONO PERSONALIZADO ---
        lblIconoLogin = new JLabel();
        lblIconoLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIconoLogin.setOpaque(false); // Transparencia activa

        try {
            // Cargamos la imagen desde recursos
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/resources/login.png"));

            // Escalado de alta calidad (120x120 es un buen tamaño estándar)
            Image imgRedimensionada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblIconoLogin.setIcon(new ImageIcon(imgRedimensionada));
        } catch (Exception e) {
            lblIconoLogin.setText("Logo no disponible");
            System.err.println("No se encontró /resources/login.png");
        }

        // Título de bienvenida
        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Configuración de campos
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        estilizarCampo(txtCorreo);
        estilizarCampo(txtPassword);

        // Navegación con flechas ↑ ↓ entre campos
        NavegacionTeclado.registrar(txtCorreo, txtPassword);

        btnLogin = new JButton("ENTRAR");
        estilizarBoton(btnLogin);

        // Montaje de componentes en el panel
        panel.add(lblIconoLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtCorreo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        // Manejo de eventos de teclado
        txtCorreo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocusInWindow();
                }
            }
        });

        // El botón ENTRAR se activa con la tecla Enter globalmente
        getRootPane().setDefaultButton(btnLogin);
    }

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

    // Getters para el controlador
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }
}//Fin de la clase
