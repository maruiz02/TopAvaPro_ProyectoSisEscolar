package itz.vista;

import itz.modelo.*;
import itz.controlador.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VentanaAdmin extends JFrame {

    private JTabbedPane pestañas;
    private ControladorAlumno ctrlAlumno;
    private ControladorAdmin ctrlAdmin;
    
    // Colores y Fuentes consistentes
    private final Color AZUL_MODERNO = new Color(63, 81, 181);
    private final Color AZUL_HOVER = new Color(48, 63, 159);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 12);

    public VentanaAdmin(ControladorAlumno ctrlAlumno, ControladorAdmin ctrlAdmin) {
        this.ctrlAlumno = ctrlAlumno;
        this.ctrlAdmin = ctrlAdmin;

        setTitle("Panel Maestro de Administración - ITZ");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pestañas = new JTabbedPane();
        pestañas.setFont(FUENTE_TITULO);

        // Agregamos las áreas de administración con iconos de texto
        pestañas.addTab("Alumnos", crearPanelAlumnos());
        pestañas.addTab("Profesores", crearPanelProfesores());
        pestañas.addTab("Usuarios", crearPanelUsuarios());
        pestañas.addTab("Materias", crearPanelMaterias());
        pestañas.addTab("Horarios", crearPanelHorarios());

        add(pestañas);
    }

    //MÉTODO PARA CREAR BOTONES ESTILIZADOS 
    private JButton crearBotonModerno(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_MODERNO);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(AZUL_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(AZUL_MODERNO);
            }
        });
        return btn;
    }

    // PANEL ALUMNOS 
    private JPanel crearPanelAlumnos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Matrícula", "Nombre", "Correo"}, 0);
        JTable tabla = estilizarTabla(new JTable(modelo));

        List<Alumno> lista = ctrlAlumno.obtenerAlumnos();
        if (lista != null) {
            for (Alumno a : lista) {
                modelo.addRow(new Object[]{a.getMatricula(), a.getNombre(), a.getCorreo()});
            }
        }

        JButton btnAdd = crearBotonModerno("Registrar Nuevo Alumno");
        
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(envolverBoton(btnAdd), BorderLayout.SOUTH);
        return panel;
    }

    // PANEL PROFESORES 
    private JPanel crearPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0);
        JTable tabla = estilizarTabla(new JTable(modelo));
        
        JButton btnAdd = crearBotonModerno("Agregar Profesor");
        btnAdd.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID:");
            String nom = JOptionPane.showInputDialog("Nombre:");
            if(id != null && nom != null) modelo.addRow(new Object[]{id, nom});
        });

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(envolverBoton(btnAdd), BorderLayout.SOUTH);
        return panel;
    }

    //PANEL USUARIOS
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Usuario", "Rol"}, 0);
        JTable tabla = estilizarTabla(new JTable(modelo));
        
        if (ctrlAdmin.getListaUsuarios() != null) {
            for (Usuario u : ctrlAdmin.getListaUsuarios()) {
                modelo.addRow(new Object[]{u.getUsuario(), u.getRol()});
            }
        }

        JButton btnNuevoU = crearBotonModerno("Crear Nuevo Usuario");
        btnNuevoU.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Nombre de usuario:");
            String pass = JOptionPane.showInputDialog("Contraseña:");
            String[] roles = {"ADMIN", "ALUMNO", "PROFESOR"};
            String rol = (String) JOptionPane.showInputDialog(null, "Rol:", "Roles", 1, null, roles, roles[0]);

            if (rol != null && ctrlAdmin.registrarUsuario(user, pass, rol).equals("OK")) {
                modelo.addRow(new Object[]{user, rol});
            }
        });

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(envolverBoton(btnNuevoU), BorderLayout.SOUTH);
        return panel;
    }

    // PANEL MATERIAS 
    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Materia", "Horario", "Profesor"}, 0);
        JTable tabla = estilizarTabla(new JTable(modelo));
        
        JButton btnAdd = crearBotonModerno("Nueva Materia");
        btnAdd.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog("Nombre Materia:");
            String hor = JOptionPane.showInputDialog("Horario:");
            if(nom != null) modelo.addRow(new Object[]{nom, hor, "Sin asignar"});
        });

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(envolverBoton(btnAdd), BorderLayout.SOUTH);
        return panel;
    }

    // PANEL CONSULTA DE HORARIOS
    private JPanel crearPanelHorarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtId = new JTextField(15);
        JButton btnBus = crearBotonModerno("Ver Horario");
        
        busqueda.add(new JLabel("ID / Matrícula: "));
        busqueda.add(txtId);
        busqueda.add(btnBus);
        
        JTextArea res = new JTextArea(10, 30);
        res.setFont(new Font("Consolas", Font.PLAIN, 14));
        res.setEditable(false);
        res.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        btnBus.addActionListener(e -> res.setText("Consultando horario para: " + txtId.getText() + "...\n(Datos cargados de materias.dat)"));

        panel.add(busqueda, BorderLayout.NORTH);
        panel.add(new JScrollPane(res), BorderLayout.CENTER);
        return panel;
    }

    //MÉTODOS DE ESTILIZADO EXTRA 
    private JTable estilizarTabla(JTable tabla) {
        tabla.setRowHeight(25);
        tabla.setSelectionBackground(new Color(232, 234, 246));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(Color.WHITE);
        return tabla;
    }

    private JPanel envolverBoton(JButton btn) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnl.add(btn);
        return pnl;
    }
}