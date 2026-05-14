package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaAlumno extends JFrame {

    // Pestaña Calificaciones
    public JTable tablaCalificaciones;
    public JLabel lblPromedio;

    // Pestaña Horario
    public JTable tablaHorario;

    // Pestaña Inscripción
    public JTable tablaMaterias;
    public JTable tablaMateriasInscritas;
    public JButton btnInscribir;
    public JButton btnCancelarInscripcion;
    public JLabel lblEstadoInscripcion;

    // Panel del perfil (foto)
    public PanelFoto panelFoto;

    // Botón de cerrar sesión
    public JButton btnCerrarSesion;

    Color colorPrimario = new Color(52, 152, 219);
    Color colorPeligro = new Color(231, 76, 60);
    Color colorExito = new Color(46, 204, 113);
    Color colorTexto = Color.WHITE;

    //Constructor
    public VentanaAlumno(String nombre, String matricula) {
        setTitle("Panel Alumno — " + nombre + " [" + matricula + "]");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        itz.App.cambiarIcono(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Pestana 1 -> Perfil
        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(235, 245, 255));

        JLabel lblTituloPerfil = new JLabel("Mi Perfil", JLabel.CENTER);
        lblTituloPerfil.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloPerfil.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, "Matrícula: " + matricula, "Alumno");

        JPanel centerPerfil = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPerfil.setBackground(new Color(235, 245, 255));
        centerPerfil.add(panelFoto);

        panelPerfil.add(lblTituloPerfil, BorderLayout.NORTH);
        panelPerfil.add(centerPerfil, BorderLayout.CENTER);

        // Pestana 2 -> Calificaciones
        JPanel panelCalif = new JPanel(new BorderLayout(10, 10));
        panelCalif.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo1 = new JLabel("Mis Calificaciones", JLabel.CENTER);
        titulo1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo1.setBorder(new EmptyBorder(0, 0, 10, 0));

        tablaCalificaciones = new JTable();
        tablaCalificaciones.setRowHeight(28);
        tablaCalificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCalificaciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel panelPromedio = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPromedio.setOpaque(false);
        lblPromedio = new JLabel("Promedio General: 0.0");
        lblPromedio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPromedio.setForeground(colorPrimario);
        panelPromedio.add(lblPromedio);

        panelCalif.add(titulo1, BorderLayout.NORTH);
        panelCalif.add(new JScrollPane(tablaCalificaciones), BorderLayout.CENTER);
        panelCalif.add(panelPromedio, BorderLayout.SOUTH);

        // Pestana 3 -> Horario
        JPanel panelHorario = new JPanel(new BorderLayout(10, 10));
        panelHorario.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo2 = new JLabel("Horario de Clases", JLabel.CENTER);
        titulo2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo2.setBorder(new EmptyBorder(0, 0, 10, 0));

        tablaHorario = new JTable();
        tablaHorario.setRowHeight(28);
        tablaHorario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHorario.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelHorario.add(titulo2, BorderLayout.NORTH);
        panelHorario.add(new JScrollPane(tablaHorario), BorderLayout.CENTER);

        //Pestana 4 -> Inscripcion
        JPanel panelInscripcion = new JPanel(new BorderLayout(10, 10));
        panelInscripcion.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setOpaque(false);
        lblEstadoInscripcion = new JLabel("Estado: Inscripción Denegada");
        lblEstadoInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstadoInscripcion.setForeground(colorPeligro);
        panelEstado.add(new JLabel("  "));
        panelEstado.add(lblEstadoInscripcion);

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

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnInscribir = crearBoton("Inscribir Materia →", colorExito);
        btnCancelarInscripcion = crearBoton("← Cancelar Inscripción", colorPeligro);
        panelBotones.add(btnInscribir);
        panelBotones.add(btnCancelarInscripcion);

        panelInscripcion.add(panelEstado, BorderLayout.NORTH);
        panelInscripcion.add(panelCentral, BorderLayout.CENTER);
        panelInscripcion.add(panelBotones, BorderLayout.SOUTH);

        // Agregando pestanas
        tabs.addTab("Mi Perfil", panelPerfil);
        tabs.addTab("Calificaciones", panelCalif);
        tabs.addTab("Mi Horario", panelHorario);
        tabs.addTab("Inscripción", panelInscripcion);

        // Barra superior con usuario y botón de cerrar sesión
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(21, 67, 96));
        barraTop.setBorder(new EmptyBorder(6, 15, 6, 15));

        JLabel lblUsuario = new JLabel("👤  " + nombre + "  |  Alumno");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(163, 228, 215));

        btnCerrarSesion = new JButton("⎋  Cerrar Sesión");
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

        barraTop.add(lblUsuario, BorderLayout.WEST);
        barraTop.add(btnCerrarSesion, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(barraTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(colorTexto);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
        return boton;
    }
}//Fin de la clase
