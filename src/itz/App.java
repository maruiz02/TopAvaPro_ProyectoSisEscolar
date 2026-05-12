package itz;

import itz.modelo.*;
import itz.vista.*;
import itz.controlador.*;
import itz.reporte.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class App {

    /**
     * Método centralizado para cambiar el icono de cualquier ventana.
     * Esto elimina a Duke de la barra de tareas y el título.
     */
    public static void cambiarIcono(JFrame ventana) {
        try {
            // Se recomienda usar .png transparente para evitar fondos blancos
            Image icon = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/resources/app.png"));
            ventana.setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Error al establecer el icono: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // 1. Configuración de renderizado (Anti-aliasing) ANTES de iniciar la UI
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // 2. Cargar datos del sistema
        SistemaEscolar sistema = SistemaEscolar.cargarSistema();

        // 3. Crear admin por defecto si no existe 
        if (sistema.getAdministradores().isEmpty()) {
            sistema.getAdministradores().add(new Administrador(1, "Admin Supremo", "admin@itz.com", "1234"));
            sistema.guardarSistema();
        }

        // 4. Hook de apagado para reportes
        Runtime.getRuntime().addShutdownHook(
            new Thread(ServicioReportes::apagar, "shutdown-reportes")
        );

        // 5. Iniciar la interfaz
        VentanaLogin vista = new VentanaLogin();
        new ControladorLogin(vista, sistema);
        vista.setVisible(true);
    }
}