package itz;

import itz.modelo.*;
import itz.vista.*;
import itz.controlador.*;
import itz.reporte.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class App {

    //Metodo para cambiar los iconos en todos las ventanas
    public static void cambiarIcono(JFrame ventana) {
        try {
            Image icon = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/resources/app.png"));
            ventana.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Error al establecer el icono: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Cargando datos del sistema
        SistemaEscolar sistema = SistemaEscolar.cargarSistema();

        // Crear admin por defecto si no existe 
        if (sistema.getAdministradores().isEmpty()) {
            sistema.getAdministradores().add(new Administrador(1, "Admin Supremo", "admin@itz.com", "1234"));
            sistema.guardarSistema();
        }

        // Hook de apagado para reportes
        Runtime.getRuntime().addShutdownHook(
                new Thread(ServicioReportes::apagar, "shutdown-reportes")
        );

        // Iniciar la interfaz
        VentanaLogin vista = new VentanaLogin();
        new ControladorLogin(vista, sistema);
        vista.setVisible(true);
    }//Fin main
}//Fin clase 
