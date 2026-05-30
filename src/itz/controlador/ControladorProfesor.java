package itz.controlador;

import itz.dao.CalificacionDAO;
import itz.dao.ProfesorDAO;
import itz.modelo.Profesor;
import itz.util.Dialogos;
import itz.vista.VentanaLogin;
import itz.vista.VentanaProfesor;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ControladorProfesor {

    private VentanaProfesor vista;
    private Profesor profesor;
    private CalificacionDAO calificacionDAO;
    private ProfesorDAO profesorDAO;

    // Materias del profesor: [clave, nombre, dia, hora]
    // Se guarda para saber qué clave corresponde al índice del combo.
    private List<Object[]> materiasList;

    public ControladorProfesor(VentanaProfesor vista, Profesor profesor) {
        this.vista         = vista;
        this.profesor      = profesor;
        this.calificacionDAO = new CalificacionDAO();
        this.profesorDAO   = new ProfesorDAO();

        // 1. Cargar datos iniciales
        cargarMisMaterias();
        cargarHorario();
        // La tabla de alumnos se carga cuando el profesor elige una materia en el combo
        actualizarTablaAlumnos();

        // 2. Cuando cambia el combo de materias, refrescar la tabla de alumnos
        vista.addComboListener(e -> actualizarTablaAlumnos());

        // 3. Botones
        vista.getBtnGuardarCalificacion().addActionListener(e -> guardarCalificacion());
        vista.getBtnRefrescarAlumnos().addActionListener(e -> actualizarTablaAlumnos());
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    // ─── Cargar materias del profesor ────────────────────────────────────────

    private void cargarMisMaterias() {
        int profesorId = profesorDAO.obtenerProfesorId(profesor.getId());
        materiasList   = profesorDAO.obtenerMateriasPorProfesor(profesorId);

        // Llenar combo y tabla "Mis Materias"
        vista.removeAllItemsCombo();
        DefaultTableModel modeloMaterias = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Día", "Hora"}, 0);

        for (Object[] fila : materiasList) {
            // combo muestra "CLAVE - Nombre"
            vista.addItemCombo(fila[0] + " - " + fila[1]);
            modeloMaterias.addRow(fila);
        }
        vista.setModeloMisMaterias(modeloMaterias);
    }

    // ─── Horario del profesor ─────────────────────────────────────────────────

    private void cargarHorario() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Día", "Hora"}, 0);
        for (Object[] fila : materiasList) {
            modelo.addRow(fila);
        }
        vista.setModeloHorario(modelo);
    }

    // ─── Tabla de alumnos según materia seleccionada en el combo ─────────────

    private void actualizarTablaAlumnos() {
        int idx = vista.getComboMateriasSelectedIndex();
        if (idx < 0 || materiasList.isEmpty()) return;

        String claveMateria = (String) materiasList.get(idx)[0];

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Matricula", "Nombre", "Calificacion (0-100)"}, 0);

        List<Object[]> alumnos = calificacionDAO.obtenerAlumnosPorMateria(claveMateria);
        for (Object[] fila : alumnos) {
            modelo.addRow(fila);
        }
        vista.setModeloAlumnos(modelo);
    }

    // ─── Guardar o actualizar calificacion ───────────────────────────────────

    private void guardarCalificacion() {
        int idx = vista.getComboMateriasSelectedIndex();
        if (idx < 0 || materiasList.isEmpty()) {
            Dialogos.advertencia(vista, "No hay ninguna materia seleccionada.", "Sin materia");
            return;
        }

        String matricula    = vista.getMatriculaAlumno().trim();
        String califTexto   = vista.getCalificacion().trim();

        if (matricula.isEmpty() || califTexto.isEmpty()) {
            Dialogos.advertencia(vista, "Completa todos los campos.", "Campos vacíos");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(califTexto);
        } catch (NumberFormatException e) {
            Dialogos.error(vista, "La calificación debe ser un número (ej: 85.5).", "Error de formato");
            return;
        }

        if (valor < 0 || valor > 100) {
            Dialogos.advertencia(vista, "La calificación debe estar entre 0 y 100.", "Valor inválido");
            return;
        }

        String claveMateria = (String) materiasList.get(idx)[0];

        boolean ok = calificacionDAO.guardarOActualizarCalificacion(matricula, claveMateria, valor);

        if (ok) {
            Dialogos.info(vista, "Calificación guardada correctamente.", "Éxito");
            vista.limpiarCamposCalificacion();
            actualizarTablaAlumnos(); // refrescar para mostrar la nota nueva
        } else {
            Dialogos.error(vista,
                    "No se pudo guardar. Verifica que la matrícula '" + matricula
                    + "' esté inscrita en esta materia.",
                    "Error");
        }
    }

    // ─── Cerrar sesion ────────────────────────────────────────────────────────

    private void cerrarSesion() {
        VentanaLogin vLogin = new VentanaLogin();
        new ControladorLogin(vLogin);
        vLogin.setVisible(true);
        vista.dispose();
    }
}
