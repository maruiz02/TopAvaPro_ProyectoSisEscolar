package itz.controlador;

import itz.modelo.*;
import itz.vista.*;
import javax.swing.*;

public class ControladorLogin {

    private VentanaLogin vista;
    private ControladorAdmin ctrlAdmin; 

    public ControladorLogin(VentanaLogin vista) {
        this.vista = vista;
        this.ctrlAdmin = new ControladorAdmin(); 

        vista.setVisible(true);
        vista.getBtnLogin().addActionListener(e -> login());
    }

    private void login() {
        String user = vista.getTxtUsuario().getText().trim();
        String contra = new String(vista.getTxtContra().getPassword());

        // Buscamos en la lista del controlador administrativo
        for (Usuario u : ctrlAdmin.getListaUsuarios()) {
            if (u.getUsuario().equalsIgnoreCase(user) && u.getContrasena().equals(contra)) {
                JOptionPane.showMessageDialog(vista, "Bienvenido " + u.getRol());
                abrirVentanaSegunRol(u);
                return;
            }
        }
        JOptionPane.showMessageDialog(vista, "Credenciales inválidas");
    }

    private void abrirVentanaSegunRol(Usuario usuario) {
        if (usuario.getRol().equalsIgnoreCase("ALUMNO")) {
            
            new VentanaAlumno(usuario.getUsuario()).setVisible(true);
        } 
        else if (usuario.getRol().equalsIgnoreCase("PROFESOR")) {
            new VentanaProfesor().setVisible(true);
        } 
        else if (usuario.getRol().equalsIgnoreCase("ADMIN")) {
            
            ControladorAlumno ctrlAlumno = new ControladorAlumno();
            new VentanaAdmin(ctrlAlumno, ctrlAdmin).setVisible(true);
        }
        vista.dispose();
    }
}