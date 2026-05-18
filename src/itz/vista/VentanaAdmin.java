package itz.vista;

import itz.util.NavegacionTeclado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaAdmin extends JFrame {

    //Campos del formulario Alumno
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JTextField txtMatricula;
    private JCheckBox chkPermitirInscripcion;

    //Campos del formulario Profesor 
    private JTextField txtNombreProfesor;
    private JTextField txtCorreoProfesor;
    private JPasswordField txtPasswordProfesor;

    //Campos de Materias / Horario 
    private JTextField txtNombreMateria;
    private JTextField txtClaveMateria;
    private JTextField txtDia;
    private JTextField txtHora;
    private JTextField txtClaveMateriaHorario;
    private JTextField txtCorreoProfesorAsignar;

    //Botones 
    private JButton btnAgregarAlumno, btnEditarAlumno, btnEliminarAlumno;
    private JButton btnActualizarPermiso;
    private JButton btnGenerarBoletinAdmin;
    private JButton btnReportesLote;
    private JButton btnAgregarProfesor, btnEditarProfesor, btnEliminarProfesor;
    private JButton btnAgregarMateria, btnCrearHorario, btnEditarMateria, btnEliminarMateria;
    private JButton btnAsignarProfesor;
    private JButton btnCerrarSesion;

    //Tablas 
    private JTable tablaAlumnos;
    private JTable tablaProfesores;
    private JTable tablaMaterias;

    //Panel de perfil 
    private PanelFoto panelFoto;

    //Colores 
    private static final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private static final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(46, 204, 113);
    private static final Color COLOR_TEXTO = Color.WHITE;

    //Constructor 
    public VentanaAdmin(String nombre, String correo) {
        setTitle("Panel Administrador " + nombre);
        setSize(1050, 720);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        itz.App.cambiarIcono(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Mi Perfil", construirPanelPerfil(nombre, correo));
        tabs.addTab("Alumnos", construirPanelAlumnos());
        tabs.addTab("Profesores", construirPanelProfesores());
        tabs.addTab("Materias y Horarios", construirPanelMaterias());
        tabs.addTab("Reportes", construirPanelReportes());

        JPanel barraTop = construirBarraTop(nombre);

        setLayout(new BorderLayout());
        add(barraTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    //API PUBLICA PARA EL CONTROLADOR 
    //Getters de datos del formulario Alumno 
    public String getNombreAlumno() {
        return txtNombre.getText().trim();
    }

    public String getCorreoAlumno() {
        return txtCorreo.getText().trim();
    }

    public String getPasswordAlumno() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getMatriculaAlumno() {
        return txtMatricula.getText().trim();
    }

    public boolean isPermitirInscripcion() {
        return chkPermitirInscripcion.isSelected();
    }

    //Getters de datos del formulario Profesor
    public String getNombreProfesor() {
        return txtNombreProfesor.getText().trim();
    }

    public String getCorreoProfesor() {
        return txtCorreoProfesor.getText().trim();
    }

    public String getPasswordProfesor() {
        return new String(txtPasswordProfesor.getPassword()).trim();
    }

    //Getters de datos de Materia / Horario
    public String getNombreMateria() {
        return txtNombreMateria.getText().trim();
    }

    public String getClaveMateria() {
        return txtClaveMateria.getText().trim();
    }

    public String getClaveMateriaHorario() {
        return txtClaveMateriaHorario.getText().trim();
    }

    public String getDiaHorario() {
        return txtDia.getText().trim();
    }

    public String getHoraHorario() {
        return txtHora.getText().trim();
    }

    public String getCorreoProfesorAsignar() {
        return txtCorreoProfesorAsignar.getText().trim();
    }

    //Operaciones sobre la tabla de Alumnos
    public int getFilaAlumnoSeleccionada() {
        return tablaAlumnos.getSelectedRow();
    }

    public String getMatriculaAlumnoEnFila(int fila) {
        return (String) tablaAlumnos.getValueAt(fila, 2);
    }

    public void setModeloAlumnos(DefaultTableModel m) {
        tablaAlumnos.setModel(m);
    }

    public void limpiarSeleccionAlumnos() {
        tablaAlumnos.clearSelection();
    }

    //Operaciones sobre la tabla de Profesores 
    public int getFilaProfesorSeleccionada() {
        return tablaProfesores.getSelectedRow();
    }

    public void setModeloProfesores(DefaultTableModel m) {
        tablaProfesores.setModel(m);
    }

    public void limpiarSeleccionProfesores() {
        tablaProfesores.clearSelection();
    }

    //Operacion sobre la tabla de Materias
    public void setModeloMaterias(DefaultTableModel m) {
        tablaMaterias.setModel(m);
    }

    //Establecer/limpiar formulario Alumno
    public void setFormularioAlumno(String nombre, String correo,
            String pass, String matricula, boolean permiso) {
        txtNombre.setText(nombre);
        txtCorreo.setText(correo);
        txtPassword.setText(pass);
        txtMatricula.setText(matricula);
        chkPermitirInscripcion.setSelected(permiso);
    }

    public void limpiarFormularioAlumno() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtMatricula.setText("");
        chkPermitirInscripcion.setSelected(false);
        tablaAlumnos.clearSelection();
    }

    //Establecer/limpiar formulario Profesor
    public void setFormularioProfesor(String nombre, String correo, String pass) {
        txtNombreProfesor.setText(nombre);
        txtCorreoProfesor.setText(correo);
        txtPasswordProfesor.setText(pass);
    }

    public void limpiarFormularioProfesor() {
        txtNombreProfesor.setText("");
        txtCorreoProfesor.setText("");
        txtPasswordProfesor.setText("");
        tablaProfesores.clearSelection();
    }

    // Suscripcion a eventos de seleccion de tablas
    public void addListenerSeleccionAlumno(ListSelectionListener l) {
        tablaAlumnos.getSelectionModel().addListSelectionListener(l);
    }

    public void addListenerSeleccionProfesor(ListSelectionListener l) {
        tablaProfesores.getSelectionModel().addListSelectionListener(l);
    }

    // -- Getters de botones (permiten al controlador suscribir listeners) --
    public JButton getBtnAgregarAlumno() {
        return btnAgregarAlumno;
    }

    public JButton getBtnEditarAlumno() {
        return btnEditarAlumno;
    }

    public JButton getBtnEliminarAlumno() {
        return btnEliminarAlumno;
    }

    public JButton getBtnActualizarPermiso() {
        return btnActualizarPermiso;
    }

    public JButton getBtnGenerarBoletinAdmin() {
        return btnGenerarBoletinAdmin;
    }

    public JButton getBtnReportesLote() {
        return btnReportesLote;
    }

    public JButton getBtnAgregarProfesor() {
        return btnAgregarProfesor;
    }

    public JButton getBtnEditarProfesor() {
        return btnEditarProfesor;
    }

    public JButton getBtnEliminarProfesor() {
        return btnEliminarProfesor;
    }

    public JButton getBtnAgregarMateria() {
        return btnAgregarMateria;
    }

    public JButton getBtnCrearHorario() {
        return btnCrearHorario;
    }

    public JButton getBtnAsignarProfesor() {
        return btnAsignarProfesor;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    // ─── Construccion de paneles (privados) ───────────────────────────────────
    private JPanel construirPanelPerfil(String nombre, String correo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 245, 235));

        JLabel titulo = new JLabel("Mi Perfil", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(new EmptyBorder(15, 0, 5, 0));

        panelFoto = new PanelFoto(nombre, correo, "Administrador");

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centro.setBackground(new Color(255, 245, 235));
        centro.add(panelFoto);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelAlumnos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(9, 2, 10, 10));
        form.setOpaque(false);

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        txtMatricula = new JTextField();

        NavegacionTeclado.registrar(txtNombre, txtCorreo, txtPassword, txtMatricula);

        btnAgregarAlumno = crearBoton("Agregar Alumno", COLOR_EXITO);
        btnEditarAlumno = crearBoton("Editar Alumno", COLOR_PRIMARIO);
        btnEliminarAlumno = crearBoton("Eliminar Alumno", COLOR_PELIGRO);

        chkPermitirInscripcion = new JCheckBox("Permitir Inscripcion al alumno seleccionado");
        chkPermitirInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkPermitirInscripcion.setForeground(new Color(39, 174, 96));
        chkPermitirInscripcion.setOpaque(false);

        btnActualizarPermiso = crearBoton("Actualizar Permiso", new Color(142, 68, 173));

        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);
        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);
        form.add(new JLabel("Contrasena:"));
        form.add(txtPassword);
        form.add(new JLabel("Matricula:"));
        form.add(txtMatricula);

        agregarEnterListener(txtMatricula, btnAgregarAlumno);

        form.add(btnAgregarAlumno);
        form.add(btnEditarAlumno);
        form.add(new JLabel());
        form.add(btnEliminarAlumno);
        form.add(chkPermitirInscripcion);
        form.add(btnActualizarPermiso);

        JLabel instruccion = new JLabel("* Selecciona una fila para editar/eliminar/permisos.");
        instruccion.setForeground(new Color(100, 100, 100));
        instruccion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        form.add(instruccion);
        form.add(new JLabel());

        tablaAlumnos = new JTable();
        tablaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnGenerarBoletinAdmin = crearBoton("Generar Boletin del Alumno Seleccionado",
                new Color(41, 128, 185));

        JPanel panelBtnBoletin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnBoletin.setOpaque(false);
        panelBtnBoletin.add(btnGenerarBoletinAdmin);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaAlumnos), BorderLayout.CENTER);
        panel.add(panelBtnBoletin, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelProfesores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setOpaque(false);

        txtNombreProfesor = new JTextField();
        txtCorreoProfesor = new JTextField();
        txtPasswordProfesor = new JPasswordField();

        NavegacionTeclado.registrar(txtNombreProfesor, txtCorreoProfesor, txtPasswordProfesor);

        btnAgregarProfesor = crearBoton("Agregar Profesor", COLOR_EXITO);
        btnEditarProfesor = crearBoton("Editar Profesor", COLOR_PRIMARIO);
        btnEliminarProfesor = crearBoton("Eliminar Profesor", COLOR_PELIGRO);

        form.add(new JLabel("Nombre Profesor:"));
        form.add(txtNombreProfesor);
        form.add(new JLabel("Correo:"));
        form.add(txtCorreoProfesor);
        form.add(new JLabel("Contrasena:"));
        form.add(txtPasswordProfesor);

        agregarEnterListener(txtPasswordProfesor, btnAgregarProfesor);

        form.add(btnAgregarProfesor);
        form.add(btnEditarProfesor);
        form.add(new JLabel());
        form.add(btnEliminarProfesor);

        JLabel instr = new JLabel("*Selecciona una fila para editar/eliminar.");
        instr.setForeground(new Color(100, 100, 100));
        instr.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        form.add(instr);
        form.add(new JLabel());

        tablaProfesores = new JTable();
        tablaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProfesores), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formularios = new JPanel(new GridLayout(1, 2, 20, 20));

        // Sub-panel Gestion de Materia
        JPanel subMateria = new JPanel(new GridLayout(5, 2, 10, 10));
        subMateria.setBorder(BorderFactory.createTitledBorder("Gestion de Materia"));

        txtNombreMateria = new JTextField();
        txtClaveMateria = new JTextField();
        btnAgregarMateria = crearBoton("Guardar", COLOR_EXITO);
        btnEditarMateria = crearBoton("Editar", COLOR_PRIMARIO);
        btnEliminarMateria = crearBoton("Eliminar", COLOR_PELIGRO);

        NavegacionTeclado.registrar(txtNombreMateria, txtClaveMateria);
        agregarEnterListener(txtClaveMateria, btnAgregarMateria);

        subMateria.add(new JLabel("Nombre:"));
        subMateria.add(txtNombreMateria);
        subMateria.add(new JLabel("Clave:"));
        subMateria.add(txtClaveMateria);
        subMateria.add(btnAgregarMateria);
        subMateria.add(btnEditarMateria);
        subMateria.add(new JLabel());
        subMateria.add(btnEliminarMateria);

        // Sub-panel Horario y Asignacion
        JPanel subHorario = new JPanel(new GridLayout(7, 2, 10, 10));
        subHorario.setBorder(BorderFactory.createTitledBorder("Horario y Asignacion"));

        txtClaveMateriaHorario = new JTextField();
        txtDia = new JTextField();
        txtHora = new JTextField();
        txtCorreoProfesorAsignar = new JTextField();
        btnCrearHorario = crearBoton("Establecer Horario", COLOR_PRIMARIO);
        btnAsignarProfesor = crearBoton("Asignar a Profesor", COLOR_EXITO);

        NavegacionTeclado.registrar(txtClaveMateriaHorario, txtDia, txtHora, txtCorreoProfesorAsignar);
        agregarEnterListener(txtHora, btnCrearHorario);
        agregarEnterListener(txtCorreoProfesorAsignar, btnAsignarProfesor);

        subHorario.add(new JLabel("Clave Materia:"));
        subHorario.add(txtClaveMateriaHorario);
        subHorario.add(new JLabel("Dia:"));
        subHorario.add(txtDia);
        subHorario.add(new JLabel("Hora:"));
        subHorario.add(txtHora);
        subHorario.add(new JLabel());
        subHorario.add(btnCrearHorario);
        subHorario.add(new JLabel("Correo Profesor:"));
        subHorario.add(txtCorreoProfesorAsignar);
        subHorario.add(new JLabel());
        subHorario.add(btnAsignarProfesor);

        formularios.add(subMateria);
        formularios.add(subHorario);

        tablaMaterias = new JTable();
        panel.add(formularios, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);
        return panel;
    }

    private JPanel construirPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(245, 250, 255));

        JLabel titulo = new JLabel("Centro de Reportes - Generacion Paralela", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(39, 174, 96));
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JTextArea desc = new JTextArea(
                "  Generacion en LOTE\n\n"
                + "  Genera boletines para TODOS los alumnos de forma simultanea.\n"
                + "  Los PDFs se guardan en la carpeta /reportes/ del proyecto.\n"
                + "  El sistema usa un pool de 4 hilos en paralelo.\n"
                + "  La ventana no se congela durante la generacion.\n"
                + "  Para un alumno individual: seleccionalo en Alumnos y usa el boton azul."
        );
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setEditable(false);
        desc.setBackground(new Color(236, 240, 241));
        desc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        btnReportesLote = crearBoton("Generar Boletines de TODOS los Alumnos (Multihilo)",
                new Color(39, 174, 96));
        btnReportesLote.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReportesLote.setPreferredSize(new Dimension(400, 50));

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBtn.setOpaque(false);
        panelBtn.add(btnReportesLote);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(desc, BorderLayout.CENTER);
        panel.add(panelBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirBarraTop(String nombre) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(44, 62, 80));
        barra.setBorder(new EmptyBorder(6, 15, 6, 15));

        JLabel lblUsuario = new JLabel("  " + nombre + "  |  Administrador");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(189, 195, 199));

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

        barra.add(lblUsuario, BorderLayout.WEST);
        barra.add(btnCerrarSesion, BorderLayout.EAST);
        return barra;
    }

    // ─── Helpers internos ────────────────────────────────────────────────────
    private JButton crearBoton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setForeground(COLOR_TEXTO);
        b.setBackground(fondo);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void agregarEnterListener(JTextField campo, JButton boton) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    boton.doClick();
                }
            }
        });
    }
}//Fin de la clase 
