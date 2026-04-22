package itz.vista;

import itz.modelo.*;
import itz.reporte.ServicioReportes;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelReportes extends JPanel {

    // Modo alumno
    private Alumno alumno;
    // Modo admin
    private SistemaEscolar sistema;

    // Botones visibles
    public JButton btnBoletin;
    public JButton btnHistorial;
    public JButton btnLote;   // Solo admin

    //  Constructor modo ALUMNO
    public PanelReportes(Alumno alumno) {
        this.alumno = alumno;
        construirModoAlumno();
    }

    //  Constructor modo ADMIN
    public PanelReportes(SistemaEscolar sistema) {
        this.sistema = sistema;
        construirModoAdmin();
    }

    //  Panel modo Alumno
    private void construirModoAlumno() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // ─ Encabezado ─
        JLabel titulo = new JLabel("📄  Generación de Reportes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(41, 128, 185));
        add(titulo, BorderLayout.NORTH);

        // ─ Panel central con botones ─
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Boletín
        btnBoletin = crearBoton(
                "📋  Boletín de Calificaciones",
                "Genera un PDF con tus calificaciones del período actual",
                new Color(41, 128, 185));
        gbc.gridy = 0;
        centro.add(crearTarjeta(btnBoletin,
                "Boletín de Calificaciones",
                "Muestra todas tus materias inscritas con su\n"
                + "calificación y estado (Aprobado / Reprobado).\n"
                + "Se guarda en la carpeta /reportes/"), gbc);

        // Historial
        btnHistorial = crearBoton(
                "📚  Historial Académico (Kardex)",
                "Genera el kardex completo con estadísticas",
                new Color(142, 68, 173));
        gbc.gridy = 1;
        centro.add(crearTarjeta(btnHistorial,
                "Historial Académico",
                "Muestra tu registro académico completo:\n"
                + "total de materias, aprobadas, reprobadas,\n"
                + "promedio acumulado y gráfica de rendimiento."), gbc);

        add(new JScrollPane(centro), BorderLayout.CENTER);

        // ─ Pie informativo ─
        JLabel info = new JLabel(
                "<html><center>Los reportes se generan en segundo plano · "
                + "La ventana no se congela · Se abren con cualquier navegador</center></html>",
                SwingConstants.CENTER);
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        add(info, BorderLayout.SOUTH);

        // Listeners 
        btnBoletin.addActionListener(e -> {
            if (alumno.getKardex().getHistorial().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aún no tienes calificaciones registradas.",
                        "Sin datos", JOptionPane.WARNING_MESSAGE);
                return;
            }//Fin if 
            ServicioReportes.generarBoletinAsync(alumno, this);
        });

        btnHistorial.addActionListener(e
                -> ServicioReportes.generarHistorialAsync(alumno, this));
    }

    //  Panel modo Admin
    private void construirModoAdmin() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel titulo = new JLabel("📊  Centro de Reportes — Administrador", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(39, 174, 96));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Botón lote
        btnLote = crearBoton(
                "⚡  Generar Boletines de Todos los Alumnos",
                "Procesamiento paralelo con " + Runtime.getRuntime().availableProcessors() + " hilos",
                new Color(39, 174, 96));
        gbc.gridy = 0;
        centro.add(crearTarjeta(btnLote,
                "Generación en Lote (Multihilo)",
                "Genera boletines PDF para TODOS los alumnos\n"
                + "del sistema de forma simultánea usando el\n"
                + "pool de hilos. Ideal para fin de semestre."), gbc);

        add(new JScrollPane(centro), BorderLayout.CENTER);

        JLabel info = new JLabel(
                "<html><center>Los reportes individuales se generan desde la tabla de alumnos "
                + "con clic derecho</center></html>",
                SwingConstants.CENTER);
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        add(info, BorderLayout.SOUTH);

        btnLote.addActionListener(e -> {
            if (sistema.getAlumnos().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay alumnos registrados.");
                return;
            }//Fin if 
            int ok = JOptionPane.showConfirmDialog(this,
                    "Se generarán " + sistema.getAlumnos().size()
                    + " boletines en paralelo.\n¿Continuar?",
                    "Confirmar generación", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                ServicioReportes.generarReportesLoteAsync(sistema, this);
            }//Fin if
        });
    }

    //  Utilidades de construcción de UI
    private JButton crearBoton(String texto, String tooltip, Color color) {
        JButton btn = new JButton(texto);
        btn.setToolTipText(tooltip);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }

    //Creando la tarjeta 
    private JPanel crearTarjeta(JButton boton, String titulo, String descripcion) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 5));
        tarjeta.setBackground(new Color(248, 249, 250));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(44, 62, 80));

        JTextArea lblDesc = new JTextArea(descripcion);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDesc.setForeground(new Color(100, 100, 100));
        lblDesc.setEditable(false);
        lblDesc.setOpaque(false);
        lblDesc.setWrapStyleWord(true);
        lblDesc.setLineWrap(true);

        JPanel textos = new JPanel(new BorderLayout(3, 3));
        textos.setOpaque(false);
        textos.add(lblTitulo, BorderLayout.NORTH);
        textos.add(lblDesc, BorderLayout.CENTER);

        tarjeta.add(textos, BorderLayout.CENTER);
        tarjeta.add(boton, BorderLayout.SOUTH);
        return tarjeta;
    }
}//Fin de la clase 
