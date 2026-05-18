package itz.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public final class Dialogos {

    //Declaracion de variables 
    private static final String TITULO_INFO = "Informacion";
    private static final String TITULO_ERROR = "Error";
    private static final String TITULO_AVISO = "Advertencia";

    // Constructor privado: clase utilitaria
    private Dialogos() {
    }

    // Informativos 
    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
                TITULO_INFO, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void info(Component padre, String mensaje, String titulo) {
        JOptionPane.showMessageDialog(padre, mensaje,
                titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // Errores
    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
                TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    public static void error(Component padre, String mensaje, String titulo) {
        JOptionPane.showMessageDialog(padre, mensaje,
                titulo, JOptionPane.ERROR_MESSAGE);
    }

    // Advertencias
    public static void advertencia(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje,
                TITULO_AVISO, JOptionPane.WARNING_MESSAGE);
    }

    public static void advertencia(Component padre, String mensaje, String titulo) {
        JOptionPane.showMessageDialog(padre, mensaje,
                titulo, JOptionPane.WARNING_MESSAGE);
    }

    // Confirmaciones
    public static boolean confirmar(Component padre, String mensaje, String titulo) {
        int resp = JOptionPane.showConfirmDialog(padre, mensaje, titulo,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resp == JOptionPane.YES_OPTION;
    }

    //Confirmaciones sensibles 
    public static boolean confirmarPeligro(Component padre, String mensaje, String titulo) {
        Object[] opciones = {"Si, continuar", "Cancelar"};
        int resp = JOptionPane.showOptionDialog(
                padre, mensaje, titulo,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, opciones, opciones[1]);   // "Cancelar" es la opcion por defecto
        return resp == 0;
    }

    // contra fuerza
    public static void avisarIntentoFallido(Component padre, int restantes) {
        if (restantes <= 0) {
            error(padre,
                    "Cuenta bloqueada por multiples intentos fallidos.\n"
                    + "Reinicia la aplicacion para volver a intentarlo.",
                    "Acceso bloqueado");
        } else {
            advertencia(padre,
                    "Credenciales incorrectas.\n"
                    + "Intentos restantes: " + restantes + "\n\n"
                    + "Admin/Profesor: usa tu correo\n"
                    + "Alumno: usa tu matricula",
                    "Credenciales invalidas");
        }
    }
}//Fin de la clase
