package itz.vista;

import itz.util.NavegacionTeclado;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaAdmin extends JFrame {

    // =========================
    // CAMPOS ALUMNO
    // =========================
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JTextField txtMatricula;
    private JCheckBox chkPermitirInscripcion;

    // =========================
    // CAMPOS PROFESOR
    // =========================
    private JTextField txtNombreProfesor;
    private JTextField txtCorreoProfesor;
    private JPasswordField txtPasswordProfesor;

    // =========================
    // CAMPOS MATERIA
    // =========================
    private JTextField txtNombreMateria;
    private JTextField txtClaveMateria;
    private JTextField txtDia;
    private JTextField txtHora;

    // =========================
    // BOTONES
    // =========================
    private JButton btnAgregarAlumno;
    private JButton btnEditarAlumno;
    private JButton btnEliminarAlumno;
    private JButton btnActualizarPermiso;

    private JButton btnAgregarProfesor;
    private JButton btnEditarProfesor;
    private JButton btnEliminarProfesor;

    private JButton btnAgregarMateria;
    private JButton btnEditarMateria;
    private JButton btnEliminarMateria;

    private JButton btnCerrarSesion;

    // =========================
    // TABLAS
    // =========================
    private JTable tablaAlumnos;
    private JTable tablaProfesores;
    private JTable tablaMaterias;

    // =========================
    // COLORES
    // =========================
    private static final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private static final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(46, 204, 113);
    private static final Color COLOR_TEXTO = Color.WHITE;

    // =========================
    // CONSTRUCTOR
    // =========================
    public VentanaAdmin(String nombre, String correo) {

        setTitle("Panel Administrador - " + nombre);
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setBackground(new Color(245, 245, 245));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Alumnos", construirPanelAlumnos());
        tabs.addTab("Profesores", construirPanelProfesores());
        tabs.addTab("Materias", construirPanelMaterias());

        JPanel barraTop = construirBarraTop(nombre);

        setLayout(new BorderLayout());

        add(barraTop, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // =========================
    // BARRA SUPERIOR
    // =========================
    private JPanel construirBarraTop(String nombre) {

        JPanel barra = new JPanel(new BorderLayout());

        barra.setBackground(new Color(21, 67, 96));
        barra.setBorder(new EmptyBorder(6, 15, 6, 15));

        JLabel lblUsuario = new JLabel("  " + nombre + "  |  Administrador");

        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(163, 228, 215));

        btnCerrarSesion = new JButton("Cerrar Sesion");

        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(192, 57, 43));
        btnCerrarSesion.setFocusPainted(false);

        btnCerrarSesion.setBorder(
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        );

        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCerrarSesion.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                btnCerrarSesion.setBackground(
                        new Color(169, 50, 38)
                );
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                btnCerrarSesion.setBackground(
                        new Color(192, 57, 43)
                );
            }
        });

        barra.add(lblUsuario, BorderLayout.WEST);
        barra.add(btnCerrarSesion, BorderLayout.EAST);

        return barra;
    }

    // =========================
    // GETTERS FORMULARIO ALUMNO
    // =========================
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

    // =========================
    // GETTERS FORMULARIO PROFESOR
    // =========================
    public String getNombreProfesor() {
        return txtNombreProfesor.getText().trim();
    }

    public String getCorreoProfesor() {
        return txtCorreoProfesor.getText().trim();
    }

    public String getPasswordProfesor() {
        return new String(txtPasswordProfesor.getPassword()).trim();
    }

    // =========================
    // GETTERS FORMULARIO MATERIA
    // =========================
    public String getNombreMateria() {
        return txtNombreMateria.getText().trim();
    }

    public String getClaveMateria() {
        return txtClaveMateria.getText().trim();
    }

    public String getDiaHorario() {
        return txtDia.getText().trim();
    }

    public String getHoraHorario() {
        return txtHora.getText().trim();
    }

    // =========================
    // GETTERS TABLAS
    // =========================
    public int getFilaAlumnoSeleccionada() {
        return tablaAlumnos.getSelectedRow();
    }

    public int getFilaProfesorSeleccionada() {
        return tablaProfesores.getSelectedRow();
    }

    public int getFilaMateriaSeleccionada() {
        return tablaMaterias.getSelectedRow();
    }

    public int getIdAlumnoEnFila(int fila) {

        return Integer.parseInt(
                tablaAlumnos.getValueAt(fila, 0).toString()
        );
    }

    public int getIdProfesorEnFila(int fila) {

        return Integer.parseInt(
                tablaProfesores.getValueAt(fila, 0).toString()
        );
    }

    public String getClaveMateriaEnFila(int fila) {

        return tablaMaterias.getValueAt(fila, 0).toString();
    }

    public String getNombreMateriaEnFila(int fila) {

        return tablaMaterias.getValueAt(fila, 1).toString();
    }

    public String getDiaMateriaEnFila(int fila) {

        Object val = tablaMaterias.getValueAt(fila, 2);

        return val != null ? val.toString() : "";
    }

    public String getHoraMateriaEnFila(int fila) {

        Object val = tablaMaterias.getValueAt(fila, 3);

        return val != null ? val.toString() : "";
    }

    public String getNombreAlumnoEnFila(int fila) {

        return tablaAlumnos.getValueAt(fila, 1).toString();
    }

    public String getMatriculaAlumnoEnFila(int fila) {

        return tablaAlumnos.getValueAt(fila, 2).toString();
    }

    public String getCorreoAlumnoEnFila(int fila) {

        return tablaAlumnos.getValueAt(fila, 3).toString();
    }

    public boolean getPermisoAlumnoEnFila(int fila) {

        return Boolean.parseBoolean(
                tablaAlumnos.getValueAt(fila, 4).toString()
        );
    }

    public String getNombreProfesorEnFila(int fila) {

        return tablaProfesores.getValueAt(fila, 1).toString();
    }

    public String getCorreoProfesorEnFila(int fila) {

        return tablaProfesores.getValueAt(fila, 2).toString();
    }

    // =========================
    // MODELOS TABLAS
    // =========================
    public void setModeloAlumnos(DefaultTableModel modelo) {
        tablaAlumnos.setModel(modelo);
    }

    public void setModeloProfesores(DefaultTableModel modelo) {
        tablaProfesores.setModel(modelo);
    }

    public void setModeloMaterias(DefaultTableModel modelo) {
        tablaMaterias.setModel(modelo);
    }

    // =========================
    // FORMULARIOS
    // =========================
    public void setFormularioAlumno(
            String nombre,
            String correo,
            String pass,
            String matricula,
            boolean permiso
    ) {

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

    public void setFormularioProfesor(
            String nombre,
            String correo,
            String pass
    ) {

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

    public void setFormularioMateria(
            String nombre,
            String clave,
            String dia,
            String hora
    ) {

        txtNombreMateria.setText(nombre);
        txtClaveMateria.setText(clave);
        txtDia.setText(dia);
        txtHora.setText(hora);
    }

    public void limpiarFormularioMateria() {

        txtNombreMateria.setText("");
        txtClaveMateria.setText("");
        txtDia.setText("");
        txtHora.setText("");

        tablaMaterias.clearSelection();
    }

    // =========================
    // LISTENERS
    // =========================
    public void addListenerSeleccionAlumno(ListSelectionListener l) {

        tablaAlumnos
                .getSelectionModel()
                .addListSelectionListener(l);
    }

    public void addListenerSeleccionProfesor(ListSelectionListener l) {

        tablaProfesores
                .getSelectionModel()
                .addListSelectionListener(l);
    }

    // =========================
    // GETTERS BOTONES
    // =========================
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

    public JButton getBtnEditarMateria() {
        return btnEditarMateria;
    }

    public JButton getBtnEliminarMateria() {
        return btnEliminarMateria;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    // =========================
    // MÉTODO CERRAR SESIÓN
    // =========================
    public void cerrarSesion(Runnable accionLogout) {

        btnCerrarSesion.addActionListener(e -> {

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {

                dispose();

                if (accionLogout != null) {
                    accionLogout.run();
                }
            }
        });
    }

    // =========================
    // PANEL ALUMNOS
    // =========================
    private JPanel construirPanelAlumnos() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JPanel form = new JPanel(
                new GridLayout(6, 2, 10, 10)
        );

        form.setOpaque(false);

        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();
        txtMatricula = new JTextField();

        chkPermitirInscripcion
                = new JCheckBox("Permitir Inscripción");

        chkPermitirInscripcion.setOpaque(false);

        NavegacionTeclado.registrar(
                txtNombre,
                txtCorreo,
                txtPassword,
                txtMatricula
        );

        btnAgregarAlumno
                = crearBoton("Agregar", COLOR_EXITO);

        btnEditarAlumno
                = crearBoton("Editar", COLOR_PRIMARIO);

        btnEliminarAlumno
                = crearBoton("Eliminar", COLOR_PELIGRO);

        btnActualizarPermiso
                = crearBoton(
                        "Actualizar Permiso",
                        new Color(142, 68, 173)
                );

        form.add(new JLabel("Nombre Completo:"));
        form.add(txtNombre);

        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);

        form.add(new JLabel("Password:"));
        form.add(txtPassword);

        form.add(new JLabel("Matrícula:"));
        form.add(txtMatricula);

        form.add(btnAgregarAlumno);
        form.add(btnEditarAlumno);

        form.add(btnEliminarAlumno);

        JPanel panelPermiso = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        panelPermiso.setOpaque(false);

        panelPermiso.add(chkPermitirInscripcion);
        panelPermiso.add(btnActualizarPermiso);

        form.add(panelPermiso);

        tablaAlumnos = new JTable();

        tablaAlumnos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaAlumnos.getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaAlumnos.getSelectedRow() != -1) {

                int fila = tablaAlumnos.getSelectedRow();

                String nombre
                        = getNombreAlumnoEnFila(fila);

                String matricula
                        = getMatriculaAlumnoEnFila(fila);

                String correo
                        = getCorreoAlumnoEnFila(fila);

                boolean permiso
                        = getPermisoAlumnoEnFila(fila);

                setFormularioAlumno(
                        nombre,
                        correo,
                        "",
                        matricula,
                        permiso
                );
            }
        });

        panel.add(form, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(tablaAlumnos),
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================
    // PANEL PROFESORES
    // =========================
    private JPanel construirPanelProfesores() {

        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JPanel form = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        form.setOpaque(false);

        txtNombreProfesor = new JTextField();
        txtCorreoProfesor = new JTextField();
        txtPasswordProfesor = new JPasswordField();

        NavegacionTeclado.registrar(
                txtNombreProfesor,
                txtCorreoProfesor,
                txtPasswordProfesor
        );

        btnAgregarProfesor
                = crearBoton("Agregar", COLOR_EXITO);

        btnEditarProfesor
                = crearBoton("Editar", COLOR_PRIMARIO);

        btnEliminarProfesor
                = crearBoton("Eliminar", COLOR_PELIGRO);

        form.add(new JLabel("Nombre:"));
        form.add(txtNombreProfesor);

        form.add(new JLabel("Correo:"));
        form.add(txtCorreoProfesor);

        form.add(new JLabel("Password:"));
        form.add(txtPasswordProfesor);

        JPanel botones = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        botones.setOpaque(false);

        botones.add(btnAgregarProfesor);
        botones.add(btnEditarProfesor);
        botones.add(btnEliminarProfesor);

        form.add(new JLabel());
        form.add(botones);

        tablaProfesores = new JTable();

        tablaProfesores.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaProfesores.getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaProfesores.getSelectedRow() != -1) {

                int fila = tablaProfesores.getSelectedRow();

                String nombre
                        = getNombreProfesorEnFila(fila);

                String correo
                        = getCorreoProfesorEnFila(fila);

                setFormularioProfesor(
                        nombre,
                        correo,
                        ""
                );
            }
        });

        panel.add(form, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(tablaProfesores),
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================
    // PANEL MATERIAS
    // =========================
    private JPanel construirPanelMaterias() {

        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        JPanel form = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        form.setOpaque(false);

        txtNombreMateria = new JTextField();
        txtClaveMateria = new JTextField();
        txtDia = new JTextField();
        txtHora = new JTextField();

        NavegacionTeclado.registrar(
                txtNombreMateria,
                txtClaveMateria,
                txtDia,
                txtHora
        );

        btnAgregarMateria
                = crearBoton(
                        "Guardar Materia",
                        COLOR_EXITO
                );

        btnEditarMateria
                = crearBoton(
                        "Editar",
                        COLOR_PRIMARIO
                );

        btnEliminarMateria
                = crearBoton(
                        "Eliminar",
                        COLOR_PELIGRO
                );

        form.add(new JLabel("Nombre Materia:"));
        form.add(txtNombreMateria);

        form.add(new JLabel("Clave Materia:"));
        form.add(txtClaveMateria);

        form.add(new JLabel("Día:"));
        form.add(txtDia);

        form.add(new JLabel("Hora:"));
        form.add(txtHora);

        JPanel botones = new JPanel(
                new GridLayout(1, 3, 10, 0)
        );

        botones.setOpaque(false);

        botones.add(btnAgregarMateria);
        botones.add(btnEditarMateria);
        botones.add(btnEliminarMateria);

        form.add(new JLabel());
        form.add(botones);

        tablaMaterias = new JTable();

        tablaMaterias.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMaterias.getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tablaMaterias.getSelectedRow() != -1) {

                int fila = tablaMaterias.getSelectedRow();

                String clave
                        = getClaveMateriaEnFila(fila);

                String nombre
                        = getNombreMateriaEnFila(fila);

                String dia
                        = getDiaMateriaEnFila(fila);

                String hora
                        = getHoraMateriaEnFila(fila);

                setFormularioMateria(
                        nombre,
                        clave,
                        dia,
                        hora
                );
            }
        });

        panel.add(form, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(tablaMaterias),
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================
    // HELPERS
    // =========================
    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton boton = new JButton(texto);

        boton.setBackground(color);
        boton.setForeground(COLOR_TEXTO);

        boton.setFocusPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    private void agregarEnterListener(
            JTextField campo,
            JButton boton
    ) {

        campo.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    boton.doClick();
                }
            }
        });
    }
}