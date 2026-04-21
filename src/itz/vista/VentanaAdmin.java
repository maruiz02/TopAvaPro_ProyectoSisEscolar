package itz.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaAdmin extends JFrame {

    //Declaracion de variables 
    public JTextField txtNombre, txtCorreo, txtPassword, txtMatricula;
    public JTextField txtNombreProfesor, txtCorreoProfesor, txtPasswordProfesor;
    public JTextField txtNombreMateria, txtClaveMateria, txtDia, txtHora, txtClaveMateriaHorario;
    public JTextField txtCorreoProfesorAsignar;

    public JButton btnAsignarProfesor;
    public JButton btnAgregarAlumno, btnEditarAlumno, btnEliminarAlumno,
            btnAgregarProfesor, btnEditarProfesor, btnEliminarProfesor,
            btnAgregarMateria, btnCrearHorario,
            btnEditarMateria, btnEliminarMateria;

    public JCheckBox chkPermitirInscripcion;

    public JButton btnActualizarPermiso;

    public JTable tablaAlumnos, tablaProfesores, tablaMaterias;

    // Panel del perfil (foto admin)
    public PanelFoto panelFoto;

    Color colorPrimario = new Color(52, 152, 219);
    Color colorPeligro = new Color(231, 76, 60);
    Color colorExito = new Color(46, 204, 113);
    Color colorTexto = Color.WHITE;

    //Constructor
    public VentanaAdmin(String nombre, String correo) {
        setTitle("Panel Administrador " + nombre);
        setSize(1050, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Pestana 1 -> MiPerfil
        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(255, 245, 235));

        JLabel lblTituloPerfil = new JLabel("Mi Perfil", JLabel.CENTER);
        lblTituloPerfil.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloPerfil.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, correo, "Administrador");

        JPanel centerPerfil = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPerfil.setBackground(new Color(255, 245, 235));
        centerPerfil.add(panelFoto);

        panelPerfil.add(lblTituloPerfil, BorderLayout.NORTH);
        panelPerfil.add(centerPerfil, BorderLayout.CENTER);

        // Pestana 2 -> Alumnos 
        JPanel panelAlumnos = new JPanel(new BorderLayout(10, 10));
        panelAlumnos.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formAlumno = new JPanel(new GridLayout(9, 2, 10, 10));
        formAlumno.setOpaque(false);

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        txtMatricula = new JTextField();

        btnAgregarAlumno = crearBoton("Agregar Alumno", colorExito);
        btnEditarAlumno = crearBoton("Editar Alumno", colorPrimario);
        btnEliminarAlumno = crearBoton("Eliminar Alumno", colorPeligro);

        chkPermitirInscripcion = new JCheckBox("Permitir Inscripción al alumno seleccionado");
        chkPermitirInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkPermitirInscripcion.setForeground(new Color(39, 174, 96));
        chkPermitirInscripcion.setOpaque(false);

        btnActualizarPermiso = crearBoton("Actualizar Permiso", new Color(142, 68, 173));

        formAlumno.add(new JLabel("Nombre Completo:"));
        formAlumno.add(txtNombre);
        formAlumno.add(new JLabel("Correo:"));
        formAlumno.add(txtCorreo);
        formAlumno.add(new JLabel("Contraseña:"));
        formAlumno.add(txtPassword);
        formAlumno.add(new JLabel("Matrícula:"));
        formAlumno.add(txtMatricula);
        formAlumno.add(btnAgregarAlumno);
        formAlumno.add(btnEditarAlumno);
        formAlumno.add(new JLabel());
        formAlumno.add(btnEliminarAlumno);
        formAlumno.add(chkPermitirInscripcion);
        formAlumno.add(btnActualizarPermiso);

        JLabel lblInstrAlumno = new JLabel("* Selecciona una fila en la tabla "
                + "para editar/eliminar/permisos.");
        lblInstrAlumno.setForeground(new Color(100, 100, 100));
        lblInstrAlumno.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        formAlumno.add(lblInstrAlumno);
        formAlumno.add(new JLabel());

        tablaAlumnos = new JTable();
        tablaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelAlumnos.add(formAlumno, BorderLayout.NORTH);
        panelAlumnos.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);

        // Pestana 3 -> Profesores
        JPanel panelProfesores = new JPanel(new BorderLayout(10, 10));
        panelProfesores.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formProfesor = new JPanel(new GridLayout(6, 2, 10, 10));
        formProfesor.setOpaque(false);

        txtNombreProfesor = new JTextField();
        txtCorreoProfesor = new JTextField();
        txtPasswordProfesor = new JPasswordField();
        btnAgregarProfesor = crearBoton("Agregar Profesor", colorExito);
        btnEditarProfesor = crearBoton("Editar Profesor", colorPrimario);
        btnEliminarProfesor = crearBoton("Eliminar Profesor", colorPeligro);

        formProfesor.add(new JLabel("Nombre Profesor:"));
        formProfesor.add(txtNombreProfesor);
        formProfesor.add(new JLabel("Correo:"));
        formProfesor.add(txtCorreoProfesor);
        formProfesor.add(new JLabel("Password:"));
        formProfesor.add(txtPasswordProfesor);
        formProfesor.add(btnAgregarProfesor);
        formProfesor.add(btnEditarProfesor);
        formProfesor.add(new JLabel());
        formProfesor.add(btnEliminarProfesor);
        JLabel lblInstrProf = new JLabel("*Selecciona una fila en la tabla para editar/eliminar.");
        lblInstrProf.setForeground(new Color(100, 100, 100));
        lblInstrProf.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        formProfesor.add(lblInstrProf);
        formProfesor.add(new JLabel());

        tablaProfesores = new JTable();
        tablaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelProfesores.add(formProfesor, BorderLayout.NORTH);
        panelProfesores.add(new JScrollPane(tablaProfesores), BorderLayout.CENTER);

        // Pestana 4 -> Materias
        JPanel panelMaterias = new JPanel(new BorderLayout(10, 10));
        panelMaterias.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 20, 20));

        JPanel subMateria = new JPanel(new GridLayout(5, 2, 10, 10));
        subMateria.setBorder(BorderFactory.createTitledBorder("Gestión de Materia"));
        txtNombreMateria = new JTextField();
        txtClaveMateria = new JTextField();
        btnAgregarMateria = crearBoton("Guardar", colorExito);
        btnEditarMateria = crearBoton("Editar", colorPrimario);
        btnEliminarMateria = crearBoton("Eliminar", colorPeligro);
        subMateria.add(new JLabel("Nombre:"));
        subMateria.add(txtNombreMateria);
        subMateria.add(new JLabel("Clave:"));
        subMateria.add(txtClaveMateria);
        subMateria.add(btnAgregarMateria);
        subMateria.add(btnEditarMateria);
        subMateria.add(new JLabel());
        subMateria.add(btnEliminarMateria);

        JPanel subHorario = new JPanel(new GridLayout(7, 2, 10, 10));
        subHorario.setBorder(BorderFactory.createTitledBorder("Horario y Asignación"));
        txtClaveMateriaHorario = new JTextField();
        txtDia = new JTextField();
        txtHora = new JTextField();
        txtCorreoProfesorAsignar = new JTextField();
        btnCrearHorario = crearBoton("Establecer Horario", colorPrimario);
        btnAsignarProfesor = crearBoton("Asignar a Profesor", colorExito);
        subHorario.add(new JLabel("Clave Materia:"));
        subHorario.add(txtClaveMateriaHorario);
        subHorario.add(new JLabel("Día:"));
        subHorario.add(txtDia);
        subHorario.add(new JLabel("Hora:"));
        subHorario.add(txtHora);
        subHorario.add(new JLabel());
        subHorario.add(btnCrearHorario);
        subHorario.add(new JLabel("Correo Profesor:"));
        subHorario.add(txtCorreoProfesorAsignar);
        subHorario.add(new JLabel());
        subHorario.add(btnAsignarProfesor);

        panelFormularios.add(subMateria);
        panelFormularios.add(subHorario);

        tablaMaterias = new JTable();
        panelMaterias.add(panelFormularios, BorderLayout.NORTH);
        panelMaterias.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);

        // Agregando Pestanas
        tabs.addTab("Mi Perfil", panelPerfil);
        tabs.addTab("Alumnos", panelAlumnos);
        tabs.addTab("Profesores", panelProfesores);
        tabs.addTab("Materias y Horarios", panelMaterias);
        add(tabs);
    }

    //Creando botones
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setForeground(colorTexto);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}//FIn de la clase
