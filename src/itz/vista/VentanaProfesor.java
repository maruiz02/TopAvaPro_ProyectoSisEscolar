package itz.vista;

import itz.controlador.ControladorProfesor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaProfesor extends JFrame {

    private ControladorProfesor controlador;
    private JTabbedPane pestañas;
    private JTextField txtNoControl, txtMateria, txtCalificacion;
    private JTable tablaAlumnos;
    private DefaultTableModel modeloAlumnos;
    private JTextArea areaHorario;

    public VentanaProfesor() {
        controlador = new ControladorProfesor();

        setTitle("Sistema Académico - Panel del Docente");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pestañas.addTab("Gestión de Calificaciones", crearPanelGestionCalificaciones());
        pestañas.addTab("Alumnos Inscritos", crearPanelAlumnosInscritos());
        pestañas.addTab("Carga de trabajo", crearPanelHorario());

        add(pestañas);
    }

    private JPanel crearPanelGestionCalificaciones() {
        // Panel principal con fondo claro
        JPanel principal = new JPanel(new GridBagLayout());
        principal.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tarjeta central 
        JPanel card = new JPanel(new GridLayout(4, 2, 15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        // Estilo de etiquetas
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        card.add(crearLabel("No. de Control:", labelFont));
        txtNoControl = crearTextField();
        card.add(txtNoControl);

        card.add(crearLabel("Materia:", labelFont));
        txtMateria = crearTextField();
        card.add(txtMateria);

        card.add(crearLabel("Calificación:", labelFont));
        txtCalificacion = crearTextField();
        card.add(txtCalificacion);

        // BOTÓN AMIGABLE 
        JButton btnGuardar = new JButton("?Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setBackground(new Color(63, 81, 181)); // Azul Índigo Moderno
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        // Efecto Hover simple
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(48, 63, 159));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(63, 81, 181));
            }
        });

        btnGuardar.addActionListener(e -> guardar());

        // Ensamblar
        gbc.gridx = 0; gbc.gridy = 0;
        principal.add(card, gbc);
        gbc.gridy = 1;
        principal.add(btnGuardar, gbc);

        return principal;
    }

    // Métodos de apoyo para diseño
    private JLabel crearLabel(String texto, Font font) {
        JLabel label = new JLabel(texto);
        label.setFont(font);
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField crearTextField() {
        JTextField tf = new JTextField(15);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private JPanel crearPanelAlumnosInscritos() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"No. Control", "Materia", "Calificación Actual"};
        modeloAlumnos = new DefaultTableModel(columnas, 0);
        tablaAlumnos = new JTable(modeloAlumnos);
        tablaAlumnos.setRowHeight(25);
        tablaAlumnos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JButton btnActualizar = new JButton("🔄 Sincronizar Lista");
        btnActualizar.addActionListener(e -> cargarListaCalificaciones());

        panel.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);
        panel.add(btnActualizar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelHorario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        areaHorario = new JTextArea();
        areaHorario.setEditable(false);
        areaHorario.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaHorario.setText("\n   PRÓXIMAS CLASES\n   ------------------------------------\n" +
                           "   • POO          | 08:00 AM\n" +
                           "   • Base Datos   | 10:00 AM");
        panel.add(new JScrollPane(areaHorario));
        return panel;
    }

    private void cargarListaCalificaciones() {
        modeloAlumnos.setRowCount(0);
        for (String registro : controlador.obtenerCalificacionesAlumno("")) { 
             modeloAlumnos.addRow(registro.split(","));
        }
    }

    private void guardar() {
        try {
            controlador.registrarCalificacion(txtNoControl.getText(), txtMateria.getText(), 
                                           Double.parseDouble(txtCalificacion.getText()));
            JOptionPane.showMessageDialog(this, "Información actualizada correctamente");
            txtNoControl.setText(""); txtMateria.setText(""); txtCalificacion.setText("");
            cargarListaCalificaciones();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Verifique los datos");
        }
    }
}