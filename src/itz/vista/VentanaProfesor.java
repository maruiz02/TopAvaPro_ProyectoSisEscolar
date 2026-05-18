package itz.vista;

import itz.util.NavegacionTeclado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaProfesor extends JFrame {

    //Campos privado
    private JComboBox<String> comboMaterias;
    private JTable tablaAlumnos;
    private JTable tablaHorario;
    private JTable tablaMissMaterias;
    private JTable tablaAlumnosProfesor;
    private JTextField txtCalificacion;
    private JTextField txtIdAlumno;
    private JButton btnGuardarCalificacion;
    private JButton btnRefrescarAlumnos;
    private JButton btnCerrarSesion;
    private PanelFoto panelFoto;

    private static final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private static final Color COLOR_EXITO    = new Color(46, 204, 113);
    private static final Color COLOR_TEXTO    = Color.WHITE;

    //Constructor
    public VentanaProfesor(String nombre, String correo) {
        setTitle("Panel Profesor - " + nombre + " [" + correo + "]");
        setSize(950, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        itz.App.cambiarIcono(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Mi Perfil",        construirPanelPerfil(nombre, correo));
        tabs.addTab("Gestion de Notas", construirPanelCalificaciones());
        tabs.addTab("Mi Horario",       construirPanelHorario());
        tabs.addTab("Mis Materias",     construirPanelMisMaterias());
        tabs.addTab("Mis Alumnos",      construirPanelMisAlumnos());

        JPanel barraTop = construirBarraTop(nombre);
        setLayout(new BorderLayout());
        add(barraTop, BorderLayout.NORTH);
        add(tabs,     BorderLayout.CENTER);
    }

    //API PUBLICA PARA EL CONTROLADOR

    public int    getComboMateriasSelectedIndex() { 
        return comboMaterias.getSelectedIndex(); 
    }
    public void   removeAllItemsCombo() { 
        comboMaterias.removeAllItems(); 
    }
    public void   addItemCombo(String item) { 
        comboMaterias.addItem(item);
    }
    public void   addComboListener(ActionListener l) { 
        comboMaterias.addActionListener(l); 
    }

    public String getMatriculaAlumno() {
        return txtIdAlumno.getText().trim(); 
    }
    public String getCalificacion() { 
        return txtCalificacion.getText().trim();
    }
    public void   limpiarCamposCalificacion() {
        txtIdAlumno.setText("");
        txtCalificacion.setText("");
    }

    public void setModeloAlumnos(DefaultTableModel m) { 
        tablaAlumnos.setModel(m); 
    }
    public void setModeloHorario(DefaultTableModel m) {
        tablaHorario.setModel(m); 
    }
    public void setModeloMisMaterias(DefaultTableModel m) { 
        tablaMissMaterias.setModel(m); 
    }
    public void setModeloAlumnosProfesor(DefaultTableModel m) { 
        tablaAlumnosProfesor.setModel(m); }
    

    public JButton getBtnGuardarCalificacion() { 
        return btnGuardarCalificacion; 
    }
    public JButton getBtnRefrescarAlumnos() { 
        return btnRefrescarAlumnos;
    }
    public JButton getBtnCerrarSesion() { 
        return btnCerrarSesion; 
    }

    //Construccion de paneles 

    private JPanel construirPanelPerfil(String nombre, String correo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 255, 240));

        JLabel titulo = new JLabel("Mi Perfil", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, correo, "Profesor");
        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centro.setBackground(new Color(240, 255, 240));
        centro.add(panelFoto);

        panel.add(titulo,  BorderLayout.NORTH);
        panel.add(centro,  BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelCalificaciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.setOpaque(false);
        norte.add(new JLabel("Seleccionar Materia:"));
        comboMaterias = new JComboBox<>();
        comboMaterias.setPreferredSize(new Dimension(250, 30));
        norte.add(comboMaterias);

        tablaAlumnos = new JTable();
        tablaAlumnos.setRowHeight(25);

        JPanel sur = new JPanel(new GridLayout(1, 5, 10, 10));
        sur.setBorder(BorderFactory.createTitledBorder("Registrar Calificacion"));
        txtIdAlumno    = new JTextField();
        txtCalificacion = new JTextField();
        btnGuardarCalificacion = crearBoton("Guardar Nota", COLOR_EXITO);

        NavegacionTeclado.registrar(txtIdAlumno, txtCalificacion);
        txtCalificacion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnGuardarCalificacion.doClick();
                }//Fin if 
            }
        });

        sur.add(new JLabel("Matricula Alumno:"));
        sur.add(txtIdAlumno);
        sur.add(new JLabel("Calificacion (0-100):"));
        sur.add(txtCalificacion);
        sur.add(btnGuardarCalificacion);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);
        panel.add(sur,   BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelHorario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Horario de Clases Asignadas", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        tablaHorario = new JTable();
        tablaHorario.setRowHeight(30);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaHorario), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelMisMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Mis Materias Registradas", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        tablaMissMaterias = new JTable();
        tablaMissMaterias.setRowHeight(28);
        tablaMissMaterias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMissMaterias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMissMaterias), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelMisAlumnos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 250, 255));

        JLabel titulo = new JLabel("Alumnos Inscritos en Mis Materias", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(41, 128, 185));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel info = new JLabel(
            "  Muestra todos los alumnos inscritos en cualquiera de tus materias.", JLabel.LEFT);
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(new Color(100, 100, 100));

        tablaAlumnosProfesor = new JTable();
        tablaAlumnosProfesor.setRowHeight(28);
        tablaAlumnosProfesor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaAlumnosProfesor.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaAlumnosProfesor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnRefrescarAlumnos = crearBoton("Refrescar Lista", new Color(41, 128, 185));

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.setOpaque(false);
        panelBtn.add(btnRefrescarAlumnos);

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(titulo, BorderLayout.NORTH);
        norte.add(info,   BorderLayout.CENTER);

        panel.add(norte,  BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaAlumnosProfesor), BorderLayout.CENTER);
        panel.add(panelBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirBarraTop(String nombre) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(27, 79, 114));
        barra.setBorder(new EmptyBorder(6, 15, 6, 15));

        JLabel lblUsuario = new JLabel("  " + nombre + "  |  Profesor");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(174, 214, 241));

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

        barra.add(lblUsuario,     BorderLayout.WEST);
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
