package itz.controlador;

import itz.modelo.*;
import itz.vista.VentanaAdmin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControladorAdmin {

    //Declaracion de variables
    private VentanaAdmin vista;
    private SistemaEscolar sistema;

    public ControladorAdmin(VentanaAdmin vista, SistemaEscolar sistema) {
        this.vista = vista;
        this.sistema = sistema;

        vista.btnAgregarAlumno.addActionListener(e -> agregarAlumno());
        vista.btnEditarAlumno.addActionListener(e -> editarAlumno());
        vista.btnEliminarAlumno.addActionListener(e -> eliminarAlumno());
        vista.btnActualizarPermiso.addActionListener(e -> actualizarPermisoInscripcion());

        vista.btnAgregarProfesor.addActionListener(e -> agregarProfesor());
        vista.btnEditarProfesor.addActionListener(e -> editarProfesor());
        vista.btnEliminarProfesor.addActionListener(e -> eliminarProfesor());

        vista.btnAgregarMateria.addActionListener(e -> agregarMateria());
        vista.btnCrearHorario.addActionListener(e -> establecerHorario());
        vista.btnAsignarProfesor.addActionListener(e -> asignarMateriaAProfesor());

        vista.tablaAlumnos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarAlumnoEnFormulario();
            }
        });
        vista.tablaProfesores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProfesorEnFormulario();
            }
        });

        cargarTablaAlumnos();
        cargarTablaProfesores();
        cargarTablaMaterias();
    }//Fin constructor

    //Parte de los Alumnos
    private void agregarAlumno() {
        //Declaracion de Variables
        String nombre = vista.txtNombre.getText().trim();
        String correo = vista.txtCorreo.getText().trim();
        String pass = vista.txtPassword.getText().trim();
        String matricula = vista.txtMatricula.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }//Fin if
        for (Alumno a : sistema.getAlumnos()) {
            //Comprobar que no se duplique la matricula
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                JOptionPane.showMessageDialog(vista, "Ya existe un alumno con esa matrícula.");
                return;
            }//Fin if
        }//Fin for
        //Agregando alumnos
        sistema.getAlumnos().add(new Alumno(nombre, correo, pass, matricula));

        //Guardando alumnos
        sistema.guardarSistema();

        //Cargando datos
        cargarTablaAlumnos();

        //limpiar formulario
        limpiarFormularioAlumno();
        JOptionPane.showMessageDialog(vista, "Alumno agregado correctamente.");
    }

    private void editarAlumno() {
        int fila = vista.tablaAlumnos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un alumno para editar.");
            return;
        }//Fin if

        //Seleccionando para los cambios
        String nombre = vista.txtNombre.getText().trim();
        String correo = vista.txtCorreo.getText().trim();
        String pass = vista.txtPassword.getText().trim();
        String matricula = vista.txtMatricula.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }//Fin if

        //Guardar los cambios
        Alumno alumno = sistema.getAlumnos().get(fila);
        alumno.setNombre(nombre);
        alumno.setCorreo(correo);
        alumno.setPassword(pass);
        alumno.setMatricula(matricula);
        sistema.guardarSistema();
        cargarTablaAlumnos();
        limpiarFormularioAlumno();
        JOptionPane.showMessageDialog(vista, "Alumno actualizado correctamente.");
    }

    private void eliminarAlumno() {
        int fila = vista.tablaAlumnos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un alumno para eliminar.");
            return;
        }//Fin if

        //Seleccionar alumno
        Alumno alumno = sistema.getAlumnos().get(fila);
        int c = JOptionPane.showConfirmDialog(vista,
                "¿Quiere eliminar al alumno \"" + alumno.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            sistema.getAlumnos().remove(fila);
            sistema.guardarSistema();
            cargarTablaAlumnos();
            limpiarFormularioAlumno();
            JOptionPane.showMessageDialog(vista, "Alumno eliminado.");
        }//Fin if
    }

    private void actualizarPermisoInscripcion() {
        int fila = vista.tablaAlumnos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un alumno para actualizar su permiso.");
            return;
        }
        Alumno alumno = sistema.getAlumnos().get(fila);
        boolean permitir = vista.chkPermitirInscripcion.isSelected();
        alumno.setInscripcionPermitida(permitir);
        sistema.guardarSistema();
        cargarTablaAlumnos();
        String msg = permitir
                ? "Inscripción PERMITIDA para " + alumno.getNombre()
                : "Inscripción DENEGADA para " + alumno.getNombre();
        JOptionPane.showMessageDialog(vista, msg);
    }

    private void cargarAlumnoEnFormulario() {
        int fila = vista.tablaAlumnos.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Alumno alumno = sistema.getAlumnos().get(fila);
        vista.txtNombre.setText(alumno.getNombre());
        vista.txtCorreo.setText(alumno.getCorreo());
        vista.txtPassword.setText(alumno.getPassword());
        vista.txtMatricula.setText(alumno.getMatricula());
        vista.chkPermitirInscripcion.setSelected(alumno.isInscripcionPermitida());
    }

    private void limpiarFormularioAlumno() {
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.txtPassword.setText("");
        vista.txtMatricula.setText("");
        vista.chkPermitirInscripcion.setSelected(false);
        vista.tablaAlumnos.clearSelection();
    }

    public void cargarTablaAlumnos() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nombre", "Correo", "Matrícula", "Inscripción"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Alumno a : sistema.getAlumnos()) {
            String permiso = a.isInscripcionPermitida() ? "Permitida" : "Denegada";
            modelo.addRow(new Object[]{a.getNombre(), a.getCorreo(), a.getMatricula(), permiso});
        }
        vista.tablaAlumnos.setModel(modelo);
    }

    // Parte de los profesores 
    private void agregarProfesor() {
        //Declaracion de Variables
        String nombre = vista.txtNombreProfesor.getText().trim();
        String correo = vista.txtCorreoProfesor.getText().trim();
        String pass = vista.txtPasswordProfesor.getText().trim();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }//Fin if

        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                JOptionPane.showMessageDialog(vista, "Ya existe un profesor con ese correo.");
                return;
            }//Fin if
        }//Fin for

        //Agregando profesores
        sistema.getProfesores().add(new Profesor(nombre, correo, pass));

        //Guardando profesores
        sistema.guardarSistema();

        //Cargando datos
        cargarTablaProfesores();

        //Limpiando formulario
        limpiarFormularioProfesor();
        JOptionPane.showMessageDialog(vista, "Profesor agregado correctamente.");
    }

    private void editarProfesor() {
        int fila = vista.tablaProfesores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un profesor para editar.");
            return;
        }//Fin if 

        //Seleccionando para los cambios
        String nombre = vista.txtNombreProfesor.getText().trim();
        String correo = vista.txtCorreoProfesor.getText().trim();
        String pass = vista.txtPasswordProfesor.getText().trim();
        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }//Fin if

        //Guardando los cambios
        Profesor profesor = sistema.getProfesores().get(fila);
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setPassword(pass);
        sistema.guardarSistema();
        cargarTablaProfesores();
        limpiarFormularioProfesor();
        JOptionPane.showMessageDialog(vista, "Profesor actualizado correctamente.");
    }

    private void eliminarProfesor() {
        int fila = vista.tablaProfesores.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona un profesor para eliminar.");
            return;
        }//Fin if 

        Profesor profesor = sistema.getProfesores().get(fila);

        int c = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar al profesor \"" + profesor.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            sistema.getProfesores().remove(fila);
            sistema.guardarSistema();
            cargarTablaProfesores();
            limpiarFormularioProfesor();
            JOptionPane.showMessageDialog(vista, "Profesor eliminado.");
        }//Fin if
    }

    private void cargarProfesorEnFormulario() {
        int fila = vista.tablaProfesores.getSelectedRow();
        if (fila < 0) {
            return;
        }
        Profesor profesor = sistema.getProfesores().get(fila);
        vista.txtNombreProfesor.setText(profesor.getNombre());
        vista.txtCorreoProfesor.setText(profesor.getCorreo());
        vista.txtPasswordProfesor.setText(profesor.getPassword());
    }

    private void limpiarFormularioProfesor() {
        vista.txtNombreProfesor.setText("");
        vista.txtCorreoProfesor.setText("");
        vista.txtPasswordProfesor.setText("");
        vista.tablaProfesores.clearSelection();
    }

    public void cargarTablaProfesores() {
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Nombre", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Profesor p : sistema.getProfesores()) {
            modelo.addRow(new Object[]{p.getNombre(), p.getCorreo()});
        }//Fin for
        vista.tablaProfesores.setModel(modelo);
    }

    // ===================================================
    // Parte de las materias
    private void agregarMateria() {
        //Declaracion de variables
        String nombre = vista.txtNombreMateria.getText().trim();
        String clave = vista.txtClaveMateria.getText().trim();

        if (nombre.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Campos vacíos.");
            return;
        }//Fin if 

        //Agregando materias
        sistema.getMaterias().add(new Materia(nombre, clave));

        //Guardando materias
        sistema.guardarSistema();
        cargarTablaMaterias();
    }

    private void establecerHorario() {
        String clave = vista.txtClaveMateriaHorario.getText();
        String dia = vista.txtDia.getText();
        String hora = vista.txtHora.getText();
        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                m.setHorario(new Horario(dia, hora));
                sistema.guardarSistema();
                cargarTablaMaterias();
                return;
            }//Fin if
        }//Fin for
        JOptionPane.showMessageDialog(vista, "Materia no encontrada.");
    }

    private void asignarMateriaAProfesor() {
        String clave = vista.txtClaveMateriaHorario.getText();
        String correo = vista.txtCorreoProfesorAsignar.getText();
        Materia mat = null;
        Profesor prof = null;
        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                mat = m;
            }//Fin if
        }//Fin for

        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                prof = p;
            }//Fin if
        }//Fin for
        if (mat == null || prof == null) {
            JOptionPane.showMessageDialog(vista, "Datos incorrectos. Verifica clave y correo.");
            return;
        }//Fin if 
        for (Materia m : prof.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                JOptionPane.showMessageDialog(vista, "Esa materia ya está asignada a ese profesor.");
                return;
            }//Fin if
        }//Fin for

        //Asignando materias
        prof.getMaterias().add(mat);
        sistema.guardarSistema();
        JOptionPane.showMessageDialog(vista, "Materia asignada al profesor correctamente.");
    }

    public void cargarTablaMaterias() {
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Nombre", "Clave", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : sistema.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "Sin horario";
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), h});
        }
        vista.tablaMaterias.setModel(modelo);
    }
}//Fin de la clase
