package itz.vista;

import itz.modelo.Alumno;
import itz.reporte.ServicioReportes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel de generación de reportes para el alumno.
 *
 * Flujo multihilo:
 *   1. El alumno pulsa "Generar Boletín" o "Generar Historial".
 *   2. El botón se deshabilita inmediatamente.
 *   3. ServicioReportes lanza la tarea en un hilo del pool.
 *   4. El ProgresoCallback actualiza JProgressBar + JLabel en el EDT.
 *   5. Al terminar (100%), el botón se rehabilita y se abre el HTML.
 */
public class PanelReportes extends JPanel {

    private final JButton       btnBoletin;
    private final JButton       btnHistorial;
    private final JProgressBar  barraProgreso;
    private final JLabel        lblEstado;
    private final ServicioReportes servicio;

    private static final Color COLOR_BOLETIN   = new Color(41,  128, 185);
    private static final Color COLOR_HISTORIAL = new Color(39,  174,  96);
    private static final Color COLOR_TEXTO     = Color.WHITE;

    public PanelReportes() {
        this.servicio = new ServicioReportes();
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 245, 245));

        // ── Título ────────────────────────────────────────────────────────────
        JLabel titulo = new JLabel("Generación de Reportes", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        // ── Botones ───────────────────────────────────────────────────────────
        btnBoletin   = crearBoton("📄 Generar Boletín de Calificaciones",  COLOR_BOLETIN);
        btnHistorial = crearBoton("📚 Generar Historial Académico",        COLOR_HISTORIAL);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnBoletin);
        panelBotones.add(btnHistorial);

        // ── Barra de progreso ─────────────────────────────────────────────────
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        barraProgreso.setForeground(COLOR_BOLETIN);
        barraProgreso.setValue(0);
        barraProgreso.setPreferredSize(new Dimension(0, 22));

        lblEstado = new JLabel("Selecciona un tipo de reporte para comenzar.", JLabel.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblEstado.setForeground(new Color(100, 100, 100));

        JPanel panelProgreso = new JPanel(new BorderLayout(0, 5));
        panelProgreso.setOpaque(false);
        panelProgreso.setBorder(new TitledBorder("Estado de generación"));
        panelProgreso.add(barraProgreso, BorderLayout.CENTER);
        panelProgreso.add(lblEstado,     BorderLayout.SOUTH);

        // ── Descripción ───────────────────────────────────────────────────────
        JTextArea descripcion = new JTextArea(
            "• Boletín de Calificaciones: muestra las notas actuales del alumno " +
            "con su promedio general.\n" +
            "• Historial Académico: incluye todas las materias inscritas, " +
            "calificadas o pendientes.\n\n" +
            "Los reportes se guardan en la carpeta 'reportes/' y se abren " +
            "automáticamente en el navegador."
        );
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descripcion.setForeground(new Color(80, 80, 80));
        descripcion.setBackground(new Color(235, 245, 255));
        descripcion.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("¿Qué genera cada reporte?"));
        panelInfo.add(descripcion);

        // ── Ensamble ──────────────────────────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setOpaque(false);
        centro.add(panelBotones,  BorderLayout.NORTH);
        centro.add(panelProgreso, BorderLayout.CENTER);
        centro.add(panelInfo,     BorderLayout.SOUTH);

        add(titulo,  BorderLayout.NORTH);
        add(centro,  BorderLayout.CENTER);
    }

    // ─── API pública ──────────────────────────────────────────────────────────

    /**
     * Conecta los botones con el alumno. Llamar desde el controlador
     * después de crear el panel.
     */
    public void configurar(Alumno alumno) {
        btnBoletin.addActionListener(e ->
            servicio.generarBoletin(alumno, barraProgreso, lblEstado, btnBoletin));

        btnHistorial.addActionListener(e ->
            servicio.generarHistorial(alumno, barraProgreso, lblEstado, btnHistorial));
    }

    /** Detiene el pool de hilos. Llamar al cerrar la ventana. */
    public void shutdown() {
        servicio.shutdown();
    }

    // Getters para compatibilidad con código existente
    public JButton getBtnBoletin()   { return btnBoletin;   }
    public JButton getBtnHistorial() { return btnHistorial; }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private JButton crearBoton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(COLOR_TEXTO);
        b.setBackground(fondo);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(fondo.darker()); }
            public void mouseExited (java.awt.event.MouseEvent e) { b.setBackground(fondo);          }
        });
        return b;
    }
}
