package itz.controlador;

import itz.dao.AlumnoDAO;
import itz.dao.MateriaDAO;
import itz.dao.ProfesorDAO;
import itz.modelo.Alumno;
import itz.modelo.Materia;
import itz.modelo.Profesor;
import itz.util.Dialogos;
import itz.vista.VentanaAdmin;
import itz.vista.VentanaLogin;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ControladorAdmin {

    private VentanaAdmin vista;
    private AlumnoDAO alumnoDAO;
    private ProfesorDAO profesorDAO;
    private MateriaDAO materiaDAO;

    public ControladorAdmin(VentanaAdmin vista) {
        this.vista       = vista;
        this.alumnoDAO   = new AlumnoDAO();
        this.profesorDAO = new ProfesorDAO();
        this.materiaDAO  = new MateriaDAO();

        // ── Listeners alumnos ──────────────────────────────────────────────
        vista.getBtnAgregarAlumno()       .addActionListener(e -> agregarAlumno());
        vista.getBtnEditarAlumno()        .addActionListener(e -> editarAlumno());
        vista.getBtnEliminarAlumno()      .addActionListener(e -> eliminarAlumno());
        vista.getBtnActualizarPermiso()   .addActionListener(e -> actualizarPermiso());

        // ── Listeners profesores ───────────────────────────────────────────
        vista.getBtnAgregarProfesor()     .addActionListener(e -> agregarProfesor());
        vista.getBtnEditarProfesor()      .addActionListener(e -> editarProfesor());
        vista.getBtnEliminarProfesor()    .addActionListener(e -> eliminarProfesor());

        // ── Listeners materias ─────────────────────────────────────────────
        vista.getBtnAgregarMateria()      .addActionListener(e -> agregarMateria());
        vista.getBtnEditarMateria()       .addActionListener(e -> editarMateria());
        vista.getBtnEliminarMateria()     .addActionListener(e -> eliminarMateria());

        // ── Cerrar sesión ──────────────────────────────────────────────────
        vista.cerrarSesion(() -> {
            VentanaLogin vLogin = new VentanaLogin();
            new ControladorLogin(vLogin);
            vLogin.setVisible(true);
        });

        // ── Cargar tablas al inicio ────────────────────────────────────────
        cargarTablaAlumnos();
        cargarTablaProfesores();
        cargarTablaMaterias();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ALUMNOS
    // ══════════════════════════════════════════════════════════════════════════

    private void agregarAlumno() {
        String nombre    = vista.getNombreAlumno();
        String correo    = vista.getCorreoAlumno();
        String matricula = vista.getMatriculaAlumno();
        String password  = vista.getPasswordAlumno();

        if (nombre.isEmpty() || correo.isEmpty()
                || matricula.isEmpty() || password.isEmpty()) {
            Dialogos.error(vista, "Todos los campos son obligatorios.", "Campos vacíos");
            return;
        }

        if (alumnoDAO.existeCorreo(correo)) {
            Dialogos.error(vista, "El correo ya está registrado.", "Correo duplicado");
            return;
        }

        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setCorreo(correo);
        alumno.setPassword(password);
        alumno.setMatricula(matricula);
        alumno.setInscripcionPermitida(vista.isPermitirInscripcion());

        if (alumnoDAO.insertarAlumno(alumno)) {
            Dialogos.info(vista, "Alumno agregado correctamente.", "Éxito");
            vista.limpiarFormularioAlumno();
            cargarTablaAlumnos();
        } else {
            Dialogos.error(vista, "No se pudo agregar el alumno.", "Error");
        }
    }

    private void editarAlumno() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona un alumno de la tabla.", "Sin selección");
            return;
        }

        String nombre    = vista.getNombreAlumno();
        String correo    = vista.getCorreoAlumno();
        String matricula = vista.getMatriculaAlumno();
        String password  = vista.getPasswordAlumno();

        if (nombre.isEmpty() || correo.isEmpty() || matricula.isEmpty()) {
            Dialogos.error(vista, "Nombre, correo y matrícula son obligatorios.", "Campos vacíos");
            return;
        }
        if (password.isEmpty()) {
            Dialogos.error(vista, "Ingresa la contraseña para confirmar los cambios.", "Contraseña requerida");
            return;
        }

        Alumno alumno = new Alumno();
        alumno.setId(vista.getIdAlumnoEnFila(fila));
        alumno.setNombre(nombre);
        alumno.setCorreo(correo);
        alumno.setMatricula(matricula);
        alumno.setPassword(password);
        // Mantener el permiso que ya tenía el alumno en la tabla
        alumno.setInscripcionPermitida(vista.getPermisoAlumnoEnFila(fila));

        if (alumnoDAO.actualizarAlumno(alumno)) {
            Dialogos.info(vista, "Alumno actualizado.", "Éxito");
            vista.limpiarFormularioAlumno();
            cargarTablaAlumnos();
        } else {
            Dialogos.error(vista, "No se pudo actualizar el alumno.", "Error");
        }
    }

    private void eliminarAlumno() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona un alumno de la tabla.", "Sin selección");
            return;
        }

        int id = vista.getIdAlumnoEnFila(fila);

        if (alumnoDAO.eliminarAlumno(id)) {
            Dialogos.info(vista, "Alumno eliminado.", "Éxito");
            vista.limpiarFormularioAlumno();
            cargarTablaAlumnos();
        } else {
            Dialogos.error(vista, "No se pudo eliminar el alumno.", "Error");
        }
    }

    private void actualizarPermiso() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona un alumno primero.", "Sin selección");
            return;
        }

        String matricula = vista.getMatriculaAlumnoEnFila(fila);
        boolean permitido = vista.isPermitirInscripcion();

        if (alumnoDAO.actualizarPermisoInscripcion(matricula, permitido)) {
            String msg = permitido ? "Inscripción habilitada." : "Inscripción deshabilitada.";
            Dialogos.info(vista, msg, "Éxito");
            cargarTablaAlumnos();
        } else {
            Dialogos.error(vista, "No se pudo actualizar el permiso.", "Error");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PROFESORES
    // ══════════════════════════════════════════════════════════════════════════

    private void agregarProfesor() {
        String nombre   = vista.getNombreProfesor();
        String correo   = vista.getCorreoProfesor();
        String password = vista.getPasswordProfesor();

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Dialogos.error(vista, "Todos los campos son obligatorios.", "Campos vacíos");
            return;
        }

        Profesor profesor = new Profesor();
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setPassword(password);

        if (profesorDAO.insertarProfesor(profesor)) {
            Dialogos.info(vista, "Profesor agregado.", "Éxito");
            vista.limpiarFormularioProfesor();
            cargarTablaProfesores();
        } else {
            Dialogos.error(vista, "No se pudo agregar el profesor.", "Error");
        }
    }

    private void editarProfesor() {
        int fila = vista.getFilaProfesorSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona un profesor de la tabla.", "Sin selección");
            return;
        }

        String nombre   = vista.getNombreProfesor();
        String correo   = vista.getCorreoProfesor();
        String password = vista.getPasswordProfesor();

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Dialogos.error(vista, "Todos los campos son obligatorios.", "Campos vacíos");
            return;
        }

        Profesor profesor = new Profesor();
        profesor.setId(vista.getIdProfesorEnFila(fila));
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setPassword(password);

        if (profesorDAO.actualizarProfesor(profesor)) {
            Dialogos.info(vista, "Profesor actualizado.", "Éxito");
            vista.limpiarFormularioProfesor();
            cargarTablaProfesores();
        } else {
            Dialogos.error(vista, "No se pudo actualizar el profesor.", "Error");
        }
    }

    private void eliminarProfesor() {
        int fila = vista.getFilaProfesorSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona un profesor de la tabla.", "Sin selección");
            return;
        }

        int id = vista.getIdProfesorEnFila(fila);

        if (profesorDAO.eliminarProfesor(id)) {
            Dialogos.info(vista, "Profesor eliminado.", "Éxito");
            vista.limpiarFormularioProfesor();
            cargarTablaProfesores();
        } else {
            Dialogos.error(vista, "No se pudo eliminar el profesor.", "Error");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MATERIAS
    // ══════════════════════════════════════════════════════════════════════════

    private void agregarMateria() {
        String clave  = vista.getClaveMateria();
        String nombre = vista.getNombreMateria();

        if (clave.isEmpty() || nombre.isEmpty()) {
            Dialogos.error(vista, "La clave y el nombre de la materia son obligatorios.", "Campos vacíos");
            return;
        }

        Materia materia = new Materia();
        materia.setClave(clave);
        materia.setNombre(nombre);
        materia.setDia(vista.getDiaHorario());
        materia.setHora(vista.getHoraHorario());

        if (materiaDAO.insertarMateria(materia)) {
            Dialogos.info(vista, "Materia agregada.", "Éxito");
            vista.limpiarFormularioMateria();
            cargarTablaMaterias();
        } else {
            Dialogos.error(vista, "No se pudo agregar la materia. ¿La clave ya existe?", "Error");
        }
    }

    private void editarMateria() {
        int fila = vista.getFilaMateriaSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona una materia de la tabla.", "Sin selección");
            return;
        }

        String nombre = vista.getNombreMateria();
        if (nombre.isEmpty()) {
            Dialogos.error(vista, "El nombre de la materia no puede estar vacío.", "Campo vacío");
            return;
        }

        Materia materia = new Materia();
        // La clave viene de la fila seleccionada, no del campo (no se puede cambiar la PK)
        materia.setClave(vista.getClaveMateriaEnFila(fila));
        materia.setNombre(nombre);
        materia.setDia(vista.getDiaHorario());
        materia.setHora(vista.getHoraHorario());

        if (materiaDAO.actualizarMateria(materia)) {
            Dialogos.info(vista, "Materia actualizada.", "Éxito");
            vista.limpiarFormularioMateria();
            cargarTablaMaterias();
        } else {
            Dialogos.error(vista, "No se pudo actualizar la materia.", "Error");
        }
    }

    private void eliminarMateria() {
        int fila = vista.getFilaMateriaSeleccionada();
        if (fila == -1) {
            Dialogos.error(vista, "Selecciona una materia de la tabla.", "Sin selección");
            return;
        }

        String clave = vista.getClaveMateriaEnFila(fila);

        if (materiaDAO.eliminarMateria(clave)) {
            Dialogos.info(vista, "Materia eliminada.", "Éxito");
            vista.limpiarFormularioMateria();
            cargarTablaMaterias();
        } else {
            Dialogos.error(vista, "No se pudo eliminar la materia.", "Error");
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CARGA DE TABLAS
    // ══════════════════════════════════════════════════════════════════════════

    private void cargarTablaAlumnos() {
        ArrayList<Alumno> lista = alumnoDAO.listarAlumnos();

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Matricula", "Correo", "Permiso"}, 0) {
            // La columna Permiso es booleana → JTable la renderiza como checkbox
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 4 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Alumno a : lista) {
            modelo.addRow(new Object[]{
                a.getId(),
                a.getNombre(),
                a.getMatricula(),
                a.getCorreo(),
                a.isInscripcionPermitida()
            });
        }
        vista.setModeloAlumnos(modelo);
    }

    private void cargarTablaProfesores() {
        ArrayList<Profesor> lista = profesorDAO.listarProfesores();

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Correo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Profesor p : lista) {
            modelo.addRow(new Object[]{ p.getId(), p.getNombre(), p.getCorreo() });
        }
        vista.setModeloProfesores(modelo);
    }

    private void cargarTablaMaterias() {
        ArrayList<Materia> lista = materiaDAO.listarMaterias();

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Nombre", "Día", "Hora"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Materia m : lista) {
            modelo.addRow(new Object[]{
                m.getClave(),
                m.getNombre(),
                m.getDia()  != null ? m.getDia()  : "",
                m.getHora() != null ? m.getHora() : ""
            });
        }
        vista.setModeloMaterias(modelo);
    }
}
