package itz.controlador;

import itz.modelo.*;
import itz.util.*;
import itz.vista.*;
import javax.swing.*;

public class ControladorLogin {

    //Declaracion de variables 
    private static final int MAX_INTENTOS = 5; //Maximo de intentos para logear al usuario 
    private VentanaLogin vista;
    private SistemaEscolar sistema;
    private int intentosFallidos = 0;

    public ControladorLogin(VentanaLogin vista, SistemaEscolar sistema) {
        this.vista = vista;
        this.sistema = sistema;
        // Usamos la API publica en lugar de acceder al campo btnLogin directamente
        this.vista.addLoginListener(e -> iniciarSesion());
    }

    //Iniciar sesion 
    private void iniciarSesion() {
        // Bloqueo por intentos
        if (intentosFallidos >= MAX_INTENTOS) {
            Dialogos.error(vista,
                    "Cuenta bloqueada por multiples intentos fallidos.\n"
                    + "Reinicia la aplicacion para volver a intentarlo.",
                    "Acceso bloqueado");
            vista.setBloqueado(true);
            return;
        }//fin if 

        String usuario = vista.getUsuario();
        String pass = vista.getPasswordTexto();

        if (usuario.isEmpty() || pass.isEmpty()) {
            Dialogos.advertencia(vista,
                    "Por favor, completa todos los campos.",
                    "Campos vacios");
            return;
        }//fin if 

        // Administrador
        for (Administrador admin : sistema.getAdministradores()) {
            if (admin.getCorreo().equalsIgnoreCase(usuario)
                    && admin.getPassword().equals(pass)) {
                abrirAdmin(admin);
                return;
            }//fin if
        }//fin for 

        // Profesor
        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(usuario)
                    && p.getPassword().equals(pass)) {
                abrirProfesor(p);
                return;
            }//fin if 
        }//fin for 

        // Alumno (login por matricula)
        for (Alumno a : sistema.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(usuario)
                    && a.getPassword().equals(pass)) {
                abrirAlumno(a);
                return;
            }//fin if 
        }//fin for 

        // Credenciales incorrectas
        intentosFallidos++;
        int restantes = MAX_INTENTOS - intentosFallidos;
        Dialogos.avisarIntentoFallido(vista, restantes);

        if (intentosFallidos >= MAX_INTENTOS) {
            vista.setBloqueado(true);
        }//fin if 
    }

    private void abrirAdmin(Administrador admin) {
        VentanaAdmin vAdmin = new VentanaAdmin(admin.getNombre(), admin.getCorreo());
        new ControladorAdmin(vAdmin, sistema);
        vAdmin.setVisible(true);
        vista.dispose();
    }

    private void abrirProfesor(Profesor p) {
        VentanaProfesor vProf = new VentanaProfesor(p.getNombre(), p.getCorreo());
        new ControladorProfesor(vProf, sistema, p);
        vProf.setVisible(true);
        vista.dispose();
    }

    private void abrirAlumno(Alumno a) {
        VentanaAlumno vAlum = new VentanaAlumno(a.getNombre(), a.getMatricula());
        new ControladorAlumno(vAlum, sistema, a);
        vAlum.setVisible(true);
        vista.dispose();
    }
}//fin de la clase 
