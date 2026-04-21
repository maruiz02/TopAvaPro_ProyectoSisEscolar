package itz.vista;

import javax.swing.*;
import java.awt.*;

/*Panel que simula la foto de perfil de un usuario dibujando el rostro de una
persona con su nombre y rol.
 */
public class PanelFoto extends JPanel {
    
    //Declaracion de variables
    private String nombre;
    private String identificador; // matricula o correo
    private String rol;           // Se muestra el rol que tiene el usuario ya sea 
                                  //"Alumno", "Profesor", "Administrador"

    // Colores personalizables por rol
    private Color colorFondo;
    private Color colorCabeza = new Color(255, 206, 158);

    public PanelFoto(String nombre, String identificador, String rol) {
        this.nombre        = nombre;
        this.identificador = identificador;
        this.rol           = rol;

        // Color de fondo segun rol
        switch (rol) {
            case "Alumno":
                colorFondo = new Color(235, 245, 255);
                break;
            case "Profesor":
                colorFondo = new Color(240, 255, 240);
                break;
            default:
                colorFondo = new Color(255, 245, 235);
        }//Fin switch

        setBackground(colorFondo);
        setPreferredSize(new Dimension(300, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;  // centro horizontal

        // ── Tarjeta de fondo ──────────────────────────────
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(cx - 120, 20, 240, 300, 25, 25);
        g2.setColor(new Color(200, 210, 230));
        g2.drawRoundRect(cx - 120, 20, 240, 300, 25, 25);

        // ── Franja superior de color por rol ──────────────
        g2.setColor(rolColor());
        g2.fillRoundRect(cx - 120, 20, 240, 60, 25, 25);
        g2.fillRect(cx - 120, 50, 240, 30); // esquinas inferiores rectas

        // ── Texto del rol en la franja ─────────────────────
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(rol, cx - fm.stringWidth(rol) / 2, 55);

        // ── Cabeza ────────────────────────────────────────
        int faceX = cx - 75;
        int faceY = 80;
        g2.setColor(colorCabeza);
        g2.fillOval(faceX, faceY, 150, 150);

        // Contorno cabeza
        g2.setColor(new Color(200, 150, 100));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(faceX, faceY, 150, 150);

        // ── Cabello ───────────────────────────────────────
        g2.setColor(new Color(80, 50, 20));
        g2.fillArc(faceX, faceY, 150, 100, 0, 180);

        // ── Ojos ──────────────────────────────────────────
        // Blanco del ojo
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - 50, faceY + 55, 32, 28);
        g2.fillOval(cx + 18, faceY + 55, 32, 28);
        // Iris
        g2.setColor(new Color(60, 100, 180));
        g2.fillOval(cx - 43, faceY + 60, 18, 18);
        g2.fillOval(cx + 25, faceY + 60, 18, 18);
        // Pupila
        g2.setColor(Color.BLACK);
        g2.fillOval(cx - 38, faceY + 65, 8, 8);
        g2.fillOval(cx + 30, faceY + 65, 8, 8);
        // Brillo
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - 35, faceY + 65, 4, 4);
        g2.fillOval(cx + 33, faceY + 65, 4, 4);

        // ── Nariz ─────────────────────────────────────────
        g2.setColor(new Color(210, 160, 110));
        int[] xn = {cx, cx + 10, cx - 10};
        int[] yn = {faceY + 85, faceY + 110, faceY + 110};
        g2.fillPolygon(xn, yn, 3);

        // ── Cejas ─────────────────────────────────────────
        g2.setColor(new Color(80, 50, 20));
        g2.setStroke(new BasicStroke(3f));
        g2.drawArc(cx - 52, faceY + 45, 36, 16, 0, 180);
        g2.drawArc(cx + 16, faceY + 45, 36, 16, 0, 180);

        // ── Boca ──────────────────────────────────────────
        g2.setColor(new Color(180, 60, 60));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawArc(cx - 30, faceY + 108, 60, 28, 0, -180);
        // Labios
        g2.setColor(new Color(220, 100, 100));
        g2.fillArc(cx - 28, faceY + 108, 56, 14, 0, 180);

        // ── Cuello ────────────────────────────────────────
        g2.setColor(colorCabeza);
        g2.fillRect(cx - 22, faceY + 140, 44, 25);

        // ── Cuerpo / torso ────────────────────────────────
        g2.setColor(rolColor());
        g2.fillRoundRect(cx - 55, faceY + 160, 110, 70, 15, 15);

        // ── Orejas ────────────────────────────────────────
        g2.setColor(colorCabeza);
        g2.fillOval(faceX - 10, faceY + 60, 18, 22);
        g2.fillOval(faceX + 142, faceY + 60, 18, 22);

        // ── Información del usuario ────────────────────────
        // Nombre
        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        fm = g2.getFontMetrics();
        g2.drawString(nombre, cx - fm.stringWidth(nombre) / 2, 260);

        // Identificador (matricula o correo)
        g2.setColor(new Color(100, 100, 100));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fm = g2.getFontMetrics();
        g2.drawString(identificador, cx - fm.stringWidth(identificador) / 2, 280);

        // Línea separadora
        g2.setColor(new Color(220, 220, 220));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(cx - 90, 293, cx + 90, 293);

        // Badge del rol
        g2.setColor(rolColor());
        g2.fillRoundRect(cx - 45, 300, 90, 20, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        fm = g2.getFontMetrics();
        g2.drawString(rol.toUpperCase(), cx - fm.stringWidth(rol.toUpperCase()) / 2, 314);
    }

    private Color rolColor() {
        switch (rol) {
            case "Alumno":        return new Color(52, 152, 219);
            case "Profesor":      return new Color(46, 204, 113);
            case "Administrador": return new Color(231, 76, 60);
            default:              return new Color(149, 165, 166);
        }
    }
}//Fin de la clase
