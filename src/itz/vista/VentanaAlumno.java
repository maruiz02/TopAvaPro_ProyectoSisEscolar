package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaAlumno extends JFrame {

    //Campos privados 
    private JTable tablaCalificaciones;
    private JLabel lblPromedio;
    private JTable tablaHorario;
    private JTable tablaMaterias;
    private JTable tablaMateriasInscritas;
    private JButton btnInscribir;
    private JButton btnCancelarInscripcion;
    private JLabel  lblEstadoInscripcion;
    private PanelFoto panelFoto;
    private JButton btnCerrarSesion;

    private static final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private static final Color COLOR_PELIGRO  = new Color(231, 76, 60);
    private static final Color COLOR_EXITO    = new Color(46, 204, 113);
    private static final Color COLOR_TEXTO    = Color.WHITE;
    
    //Constructor
    public VentanaAlumno(String nombre, String matricula) {
        setTitle("Panel Alumno - " + nombre + " [" + matricula + "]");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        itz.App.cambiarIcono(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Mi Perfil",       construirPanelPerfil(nombre, matricula));
        tabs.addTab("Calificaciones",  construirPanelCalificaciones());
        tabs.addTab("Mi Horario",      construirPanelHorario());
        tabs.addTab("Inscripcion",     construirPanelInscripcion());

        JPanel barraTop = construirBarraTop(nombre);
        setLayout(new BorderLayout());
        add(barraTop, BorderLayout.NORTH);
        add(tabs,     BorderLayout.CENTER);
    }

    //API PUBLICA PARA EL CONTROLADOR 

    public void setModeloCalificaciones(DefaultTableModel m) { 
        tablaCalificaciones.setModel(m); 
    }
    public void setModeloHorario(DefaultTableModel m) { 
        tablaHorario.setModel(m); 
    }
    public void setModeloMateriasDisponibles(DefaultTableModel m) { 
        tablaMaterias.setModel(m); 
    }
    public void setModeloMateriasInscritas(DefaultTableModel m) { 
        tablaMateriasInscritas.setModel(m); 
    }

    public void setPromedio(String texto) { 
        lblPromedio.setText(texto); 
    }
    public void setEstadoInscripcion(String texto, Color color) {
        lblEstadoInscripcion.setText(texto);
        lblEstadoInscripcion.setForeground(color);
    }

    public void setBotonesInscripcionHabilitados(boolean habilitado) {
        btnInscribir.setEnabled(habilitado);
        btnCancelarInscripcion.setEnabled(habilitado);
    }

    public String getClaveMateriaSelecionadaDisponible() {
        int fila = tablaMaterias.getSelectedRow();
        if (fila < 0) {
            return null;
        }//Fin if 
        return (String) tablaMaterias.getValueAt(fila, 1);
    }

    public String getClaveMateriaSelecionadaInscrita() {
        int fila = tablaMateriasInscritas.getSelectedRow();
        if (fila < 0) {
            return null;
        }//Fin if 
        return (String) tablaMateriasInscritas.getValueAt(fila, 1);
    }

    public JButton getBtnInscribir()           { return btnInscribir; }
    public JButton getBtnCancelarInscripcion() { return btnCancelarInscripcion; }
    public JButton getBtnCerrarSesion()        { return btnCerrarSesion; }

    //Construccion de paneles

    private JPanel construirPanelPerfil(String nombre, String matricula) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(235, 245, 255));

        JLabel titulo = new JLabel("Mi Perfil", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, "Matricula: " + matricula, "Alumno");
        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centro.setBackground(new Color(235, 245, 255));
        centro.add(panelFoto);

        panel.add(titulo,  BorderLayout.NORTH);
        panel.add(centro,  BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelCalificaciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Mis Calificaciones", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        tablaCalificaciones = new JTable();
        tablaCalificaciones.setRowHeight(28);
        tablaCalificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCalificaciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel panelPromedio = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPromedio.setOpaque(false);
        lblPromedio = new JLabel("Promedio General: 0.0");
        lblPromedio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPromedio.setForeground(COLOR_PRIMARIO);
        panelPromedio.add(lblPromedio);

        panel.add(titulo,  BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCalificaciones), BorderLayout.CENTER);
        panel.add(panelPromedio, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelHorario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Horario de Clases", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        tablaHorario = new JTable();
        tablaHorario.setRowHeight(28);
        tablaHorario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHorario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(titulo,  BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelInscripcion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Estado de inscripcion
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setOpaque(false);
        lblEstadoInscripcion = new JLabel("Estado: Inscripcion Denegada");
        lblEstadoInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstadoInscripcion.setForeground(COLOR_PELIGRO);
        panelEstado.add(new JLabel("  "));
        panelEstado.add(lblEstadoInscripcion);

        // Tablas lado a lado
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));

        JPanel panelDisponibles = new JPanel(new BorderLayout(5, 5));
        panelDisponibles.setBorder(BorderFactory.createTitledBorder("Materias Disponibles"));
        tablaMaterias = new JTable();
        tablaMaterias.setRowHeight(25);
        tablaMaterias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMaterias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelDisponibles.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);

        JPanel panelInscritas = new JPanel(new BorderLayout(5, 5));
        panelInscritas.setBorder(BorderFactory.createTitledBorder("Materias Inscritas"));
        tablaMateriasInscritas = new JTable();
        tablaMateriasInscritas.setRowHeight(25);
        tablaMateriasInscritas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMateriasInscritas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaMateriasInscritas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelInscritas.add(new JScrollPane(tablaMateriasInscritas), BorderLayout.CENTER);

        panelCentral.add(panelDisponibles);
        panelCentral.add(panelInscritas);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnInscribir          = crearBoton("Inscribir Materia ->",    COLOR_EXITO);
        btnCancelarInscripcion = crearBoton("<- Cancelar Inscripcion", COLOR_PELIGRO);
        panelBotones.add(btnInscribir);
        panelBotones.add(btnCancelarInscripcion);

        panel.add(panelEstado,  BorderLayout.NORTH);
        panel.add(panelCentral, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirBarraTop(String nombre) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(21, 67, 96));
        barra.setBorder(new EmptyBorder(6, 15, 6, 15));

        JLabel lblUsuario = new JLabel("  " + nombre + "  |  Alumno");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(163, 228, 215));

        btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(192, 57, 43));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(169, 50, 38));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(192, 57, 43));
            }
        });

        barra.add(lblUsuario,      BorderLayout.WEST);
        barra.add(btnCerrarSesion, BorderLayout.EAST);
        return barra;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(COLOR_TEXTO);
        b.setBackground(fondo);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(fondo.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(fondo); }
        });
        return b;
    }
}//Fin de la clase 
