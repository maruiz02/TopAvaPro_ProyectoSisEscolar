package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaProfesor extends JFrame {
    
    //Declaracion de variables
    public JComboBox<String> comboMaterias;
    public JTable tablaAlumnos, tablaHorario, tablaMissMaterias;
    public JTextField txtCalificacion, txtIdAlumno;
    public JButton btnGuardarCalificacion;

    // Panel del perfil (foto)
    public PanelFoto panelFoto;

    Color colorPrimario = new Color(52, 152, 219);
    Color colorExito    = new Color(46, 204, 113);
    Color colorTexto    = Color.WHITE;
    
    //Constructor
    public VentanaProfesor(String nombre, String correo) {
        setTitle("Panel Profesor — " + nombre + " [" + correo + "]");
        setSize(950, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        //Pestana 1 -> Mi Pefil
        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(240, 255, 240));

        JLabel lblTituloPerfil = new JLabel("Mi Perfil", JLabel.CENTER);
        lblTituloPerfil.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloPerfil.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, correo, "Profesor");

        JPanel centerPerfil = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPerfil.setBackground(new Color(240, 255, 240));
        centerPerfil.add(panelFoto);

        panelPerfil.add(lblTituloPerfil, BorderLayout.NORTH);
        panelPerfil.add(centerPerfil,    BorderLayout.CENTER);

        // Pestana 2 -> Gestion de notas 
        JPanel panelCalificaciones = new JPanel(new BorderLayout(15, 15));
        panelCalificaciones.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel norteCalificaciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norteCalificaciones.setOpaque(false);
        norteCalificaciones.add(new JLabel("Seleccionar Materia:"));
        comboMaterias = new JComboBox<>();
        comboMaterias.setPreferredSize(new Dimension(250, 30));
        norteCalificaciones.add(comboMaterias);

        tablaAlumnos = new JTable();
        tablaAlumnos.setRowHeight(25);
        JScrollPane scrollAlumnos = new JScrollPane(tablaAlumnos);

        JPanel surCalificaciones = new JPanel(new GridLayout(1, 5, 10, 10));
        surCalificaciones.setBorder(BorderFactory.createTitledBorder("Registrar Calificación"));
        txtIdAlumno            = new JTextField();
        txtCalificacion        = new JTextField();
        btnGuardarCalificacion = crearBoton("Guardar Nota", colorExito);
        surCalificaciones.add(new JLabel("Matrícula Alumno:"));
        surCalificaciones.add(txtIdAlumno);
        surCalificaciones.add(new JLabel("Calificación (0-10):"));
        surCalificaciones.add(txtCalificacion);
        surCalificaciones.add(btnGuardarCalificacion);

        panelCalificaciones.add(norteCalificaciones, BorderLayout.NORTH);
        panelCalificaciones.add(scrollAlumnos,       BorderLayout.CENTER);
        panelCalificaciones.add(surCalificaciones,   BorderLayout.SOUTH);

        // Pestana 3 -> Mi Horario
        JPanel panelHorario = new JPanel(new BorderLayout(10, 10));
        panelHorario.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lblTituloHorario = new JLabel("Horario de Clases Asignadas", JLabel.CENTER);
        lblTituloHorario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tablaHorario = new JTable();
        tablaHorario.setRowHeight(30);
        panelHorario.add(lblTituloHorario,              BorderLayout.NORTH);
        panelHorario.add(new JScrollPane(tablaHorario), BorderLayout.CENTER);

        // Pestana 4 -> Mis Materias
        JPanel panelMisMaterias = new JPanel(new BorderLayout(10, 10));
        panelMisMaterias.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel lblTituloMaterias = new JLabel("Mis Materias Registradas", JLabel.CENTER);
        lblTituloMaterias.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloMaterias.setBorder(new EmptyBorder(0, 0, 15, 0));
        tablaMissMaterias = new JTable();
        tablaMissMaterias.setRowHeight(28);
        tablaMissMaterias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMissMaterias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelMisMaterias.add(lblTituloMaterias,                  BorderLayout.NORTH);
        panelMisMaterias.add(new JScrollPane(tablaMissMaterias), BorderLayout.CENTER);

        // Agregando pestanas
        tabs.addTab("Mi Perfil",       panelPerfil);
        tabs.addTab("Gestión de Notas", panelCalificaciones);
        tabs.addTab("Mi Horario",       panelHorario);
        tabs.addTab("Mis Materias",     panelMisMaterias);

        add(tabs);
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
            public void mouseEntered(java.awt.event.MouseEvent evt) { boton.setBackground(colorFondo.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { boton.setBackground(colorFondo); }
        });
        return boton;
    }
}//Fin de la clase 
