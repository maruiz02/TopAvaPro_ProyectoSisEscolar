package itz.vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author marco
 */

public class VentanaLogin extends JFrame {
    //Declaraciones
    JFrame ventana = new JFrame();
    JLabel lblUsario = new JLabel("Usuario");
    JLabel lblContra = new JLabel("Contraseña");
    JTextField txtUsuario = new JTextField();
    JPasswordField txtContra = new JPasswordField();
    JButton btnLogin = new JButton("Login");

    public VentanaLogin() {
        //Atributos de la ventana
        ventana.setTitle("LOGIN");
        ventana.setBounds(0, 0, 400, 450);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Labels
        lblUsario.setBounds(50, 30, 200, 30);
        ventana.add(lblUsario);
        lblContra.setBounds(50, 110, 200, 30);
        ventana.add(lblContra);

        //Cuadros de texto
        txtUsuario.setBounds(50, 60, 300, 30);
        ventana.add(txtUsuario);
        txtContra.setBounds(50, 140, 300, 30);
        ventana.add(txtContra);

        //Botones
        btnLogin.setBounds(100, 220, 200, 40);
        ventana.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txtUsuario.getText().toUpperCase().trim();
                String contra = new String(txtContra.getPassword());
                switch (user) {
                    case "PROFESOR":
                        if (contra.equals("123")) {
                            AbrirVentanaProfesor();
                        }//Fin del if
                        else {
                            JOptionPane.showMessageDialog(null,"ERROR!","Credeciales Invalidas!", JOptionPane.WARNING_MESSAGE);
                        }//Fin del esle
                        break;
                    case "ALUMNO":
                        if (contra.equals("321")) {
                            AbrirVentanaAlumno();
                        }//Fin del if
                        else {
                            JOptionPane.showMessageDialog(null,"ERROR!","Credeciales Invalidas!", JOptionPane.WARNING_MESSAGE);
                        }//Fin del else
                        break;
                    case "ADMIN":
                        if (contra.equals("ABC")) {
                            AbrirVentanaAdmin();
                        }//Fin del if
                        else {
                            JOptionPane.showMessageDialog(null,"ERROR!","Credeciales Invalidas!", JOptionPane.WARNING_MESSAGE);
                        }//Fin del else
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"ERROR!","Credeciales Invalidas!", JOptionPane.WARNING_MESSAGE);
                        break;
                }//Fin del switch
            }//Fin del action performed
        });//Fiin del action listener
        ventana.setVisible(true);
    }//Fin del constructor
    public void AbrirVentanaProfesor() {
        VentanaProfesor ventanaP = new VentanaProfesor();
        ventanaP.setVisible(true);
        this.dispose();
    }//Fin del metodo abrir ventana Profesor

    public void AbrirVentanaAlumno() {
        VentanaAlumno ventanaA = new VentanaAlumno();
        ventanaA.setVisible(true);
        this.dispose();
    }//Fin del metodo abrir ventana alumno

    public void AbrirVentanaAdmin() {
        VentanaAdmin ventanaAa = new VentanaAdmin();
        ventanaAa.setVisible(true);
        this.dispose();
    }//Fin del metodo abrir ventana admin

    static void main(String[] args) {//Por el momento
        VentanaLogin ventana = new VentanaLogin();
        ventana.setVisible(true);
    }//Fin del metodo main
}//Fin de la clase
