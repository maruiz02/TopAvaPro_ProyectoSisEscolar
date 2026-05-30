package itz;

import itz.config.ConexionDB;
import itz.controlador.ControladorLogin;
import itz.dao.AdministradorDAO;
import itz.modelo.Administrador;
import itz.vista.VentanaLogin;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class App {

    public static void cambiarIcono(JFrame ventana) {

        try {

            Image icon = Toolkit
                    .getDefaultToolkit()
                    .getImage(
                            App.class.getResource(
                                    "/resources/app.png"
                            )
                    );

            ventana.setIconImage(icon);

        } catch (Exception e) {

            System.err.println(
                    "Error al establecer icono: "
                    + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {

        System.setProperty(
                "awt.useSystemAAFontSettings",
                "on"
        );

        System.setProperty(
                "swing.aatext",
                "true"
        );

        // Verificar conexion
        if (ConexionDB.conectar() == null) {

            System.out.println(
                    "No se pudo conectar a la base de datos"
            );

            return;
        }

        // Crear administrador por defecto
        AdministradorDAO adminDAO =
                new AdministradorDAO();

        if (adminDAO.listarAdministradores()
                .isEmpty()) {

            Administrador admin =
                    new Administrador();

            admin.setNombre(
                    "Admin Supremo"
            );

            admin.setCorreo(
                    "admin@itz.com"
            );

            admin.setPassword(
                    "1234"
            );

            adminDAO.insertarAdministrador(
                    admin
            );

            System.out.println(
                    "Administrador por defecto creado"
            );
        }

        // Abrir login
        VentanaLogin vista =
                new VentanaLogin();

        cambiarIcono(vista);

        new ControladorLogin(vista);

        vista.setVisible(true);
    }
}
