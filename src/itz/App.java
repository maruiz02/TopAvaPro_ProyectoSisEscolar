package itz;

import itz.modelo.*;
import itz.vista.*;
import itz.controlador.*;
import itz.reporte.*;

public class App {

    public static void main(String[] args) {
        // Cargar datos del sistema
        SistemaEscolar sistema = SistemaEscolar.cargarSistema();

        // Crear un admin si no existe 
        if (sistema.getAdministradores().isEmpty()) {
            sistema.getAdministradores().add(new Administrador(1, "Admin Supremo", "admin@itz.com", "1234"));
            sistema.guardarSistema();
        }//Fin if

        // Apagar el pool de hilos limpiamente al cerrar la app
        Runtime.getRuntime().addShutdownHook(
            new Thread(ServicioReportes::apagar, "shutdown-reportes")
        );

        // Iniciar interfaz
        VentanaLogin vista = new VentanaLogin();
        new ControladorLogin(vista, sistema);
        vista.setVisible(true);
    }//Fin main
}// Fin de la clase