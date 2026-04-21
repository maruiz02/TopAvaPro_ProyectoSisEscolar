package itz.controlador;

import itz.modelo.*;
import itz.vista.*;
import javax.swing.*;

public class ControladorLogin {

    //Declaracion de variables
    private VentanaLogin vista;
    private SistemaEscolar sistema;

    public ControladorLogin(VentanaLogin vista, SistemaEscolar sistema) {
        this.vista = vista;
        this.sistema = sistema;
        this.vista.btnLogin.addActionListener(e -> iniciarSesion());
    }

    //Iniciando sesion 
    private void iniciarSesion() {
        String usuario = vista.getTxtCorreo().getText().trim();
        String pass = new String(vista.getTxtPassword().getPassword());

        if (usuario.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.");
            return;
        }//Fin if

        // Para el administrador
        for (Administrador admin : sistema.getAdministradores()) {
            if (admin.getCorreo().equalsIgnoreCase(usuario) && admin.getPassword().equals(pass)) {
                abrirAdmin(admin);
                return;
            }//Fin if
        }//Fin for

        //Para el profesor
        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(usuario) && p.getPassword().equals(pass)) {
                abrirProfesor(p);
                return;
            }//Fin if 
        }//Fin for

        //Para el alumno -> login por matricula
        for (Alumno a : sistema.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(usuario) && a.getPassword().equals(pass)) {
                abrirAlumno(a);
                return;
            }//Fin if 
        }//Fin for

        JOptionPane.showMessageDialog(vista,
                "Credenciales incorrectas.\n"
                + "Admin/Profesor: usar correo\n"
                + "Alumno: usar matrícula");
    }

    //Abriendo ventana admin
    private void abrirAdmin(Administrador admin) {
        VentanaAdmin vAdmin = new VentanaAdmin(admin.getNombre(), admin.getCorreo());
        new ControladorAdmin(vAdmin, sistema);
        vAdmin.setVisible(true);
        vista.dispose();
    }

    //Abriendo ventana profesor
    private void abrirProfesor(Profesor p) {
        VentanaProfesor vProf = new VentanaProfesor(p.getNombre(), p.getCorreo());
        new ControladorProfesor(vProf, sistema, p);
        vProf.setVisible(true);
        vista.dispose();
    }

    //Abriendo ventana alumnos
    private void abrirAlumno(Alumno a) {
        VentanaAlumno vAlum = new VentanaAlumno(a.getNombre(), a.getMatricula());
        new ControladorAlumno(vAlum, sistema, a);
        vAlum.setVisible(true);
        vista.dispose();
    }
}//Fin de la clase 
