package itz.controlador;

import itz.modelo.*;
import itz.reporte.*;
import itz.util.*;
import itz.vista.*;
import javax.swing.table.DefaultTableModel;

public class ControladorAdmin {
    
    //Declaracion de las variables 
    private VentanaAdmin vista;
    private SistemaEscolar sistema;

    public ControladorAdmin(VentanaAdmin vista, SistemaEscolar sistema) {
        this.vista = vista;
        this.sistema = sistema;

        // Suscripcion a botones a traves de getters 
        vista.getBtnAgregarAlumno().addActionListener(e -> agregarAlumno());
        vista.getBtnEditarAlumno().addActionListener(e -> editarAlumno());
        vista.getBtnEliminarAlumno().addActionListener(e -> eliminarAlumno());
        vista.getBtnActualizarPermiso().addActionListener(e -> actualizarPermisoInscripcion());
        vista.getBtnGenerarBoletinAdmin().addActionListener(e -> generarBoletinAlumnoSeleccionado());
        vista.getBtnReportesLote().addActionListener(e -> generarTodosLosReportes());
        vista.getBtnAgregarProfesor().addActionListener(e -> agregarProfesor());
        vista.getBtnEditarProfesor().addActionListener(e -> editarProfesor());
        vista.getBtnEliminarProfesor().addActionListener(e -> eliminarProfesor());
        vista.getBtnAgregarMateria().addActionListener(e -> agregarMateria());
        vista.getBtnCrearHorario().addActionListener(e -> establecerHorario());
        vista.getBtnAsignarProfesor().addActionListener(e -> asignarMateriaAProfesor());
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());

        vista.addListenerSeleccionAlumno(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarAlumnoEnFormulario();
            }//fin if
        });
        vista.addListenerSeleccionProfesor(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProfesorEnFormulario();
            }//fin if
        });

        cargarTablaAlumnos();
        cargarTablaProfesores();
        cargarTablaMaterias();
    }

    // Apartado de los alumnos
    private void agregarAlumno() {
        String nombre = vista.getNombreAlumno();
        String correo = vista.getCorreoAlumno();
        String pass = vista.getPasswordAlumno();
        String matricula = vista.getMatriculaAlumno();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || matricula.isEmpty()) {
            Dialogos.advertencia(vista, "Completa todos los campos.", "Campos vacios");
            return;
        }//fin if

        String errorPass = ValidadorPassword.validar(pass);
        if (errorPass != null) {
            Dialogos.advertencia(vista,
                    errorPass + "\n\n" + ValidadorPassword.obtenerRequisitos(),
                    "Contrasena insegura");
            return;
        }//fin if

        for (Alumno a : sistema.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                Dialogos.advertencia(vista, "Ya existe un alumno con esa matricula.", "Duplicado");
                return;
            }//fin if
        }//fin for

        sistema.getAlumnos().add(new Alumno(nombre, correo, pass, matricula));
        sistema.guardarSistema();
        cargarTablaAlumnos();
        vista.limpiarFormularioAlumno();
        Dialogos.info(vista, "Alumno agregado correctamente.", "Exito");
    }

    private void editarAlumno() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un alumno para editar.", "Sin seleccion");
            return;
        }//fin if

        String nombre = vista.getNombreAlumno();
        String correo = vista.getCorreoAlumno();
        String pass = vista.getPasswordAlumno();
        String matricula = vista.getMatriculaAlumno();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty() || matricula.isEmpty()) {
            Dialogos.advertencia(vista, "Completa todos los campos.", "Campos vacios");
            return;
        }//fin if

        String errorPass = ValidadorPassword.validar(pass);
        if (errorPass != null) {
            Dialogos.advertencia(vista,
                    errorPass + "\n\n" + ValidadorPassword.obtenerRequisitos(),
                    "Contrasena insegura");
            return;
        }//fin if

        Alumno alumno = sistema.getAlumnos().get(fila);
        alumno.setNombre(nombre);
        alumno.setCorreo(correo);
        alumno.setPassword(pass);
        alumno.setMatricula(matricula);
        sistema.guardarSistema();
        cargarTablaAlumnos();
        vista.limpiarFormularioAlumno();
        Dialogos.info(vista, "Alumno actualizado correctamente.", "Exito");
    }

    private void eliminarAlumno() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un alumno para eliminar.", "Sin seleccion");
            return;
        }//fin if

        Alumno alumno = sistema.getAlumnos().get(fila);
        boolean confirma = Dialogos.confirmarPeligro(vista,
                "Eliminar al alumno \"" + alumno.getNombre() + "\"?\nEsta accion no se puede deshacer.",
                "Confirmar eliminacion");

        if (confirma) {
            sistema.getAlumnos().remove(fila);
            sistema.guardarSistema();
            cargarTablaAlumnos();
            vista.limpiarFormularioAlumno();
            Dialogos.info(vista, "Alumno eliminado.", "Eliminado");
        }//fin if
    }

    private void actualizarPermisoInscripcion() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un alumno para actualizar su permiso.", "Sin seleccion");
            return;
        }//fin if
        Alumno alumno = sistema.getAlumnos().get(fila);
        boolean permitir = vista.isPermitirInscripcion();
        alumno.setInscripcionPermitida(permitir);
        sistema.guardarSistema();
        cargarTablaAlumnos();
        String msg = permitir
                ? "Inscripcion PERMITIDA para " + alumno.getNombre()
                : "Inscripcion DENEGADA para " + alumno.getNombre();
        Dialogos.info(vista, msg, "Permiso actualizado");
    }

    private void cargarAlumnoEnFormulario() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila < 0) {
            return;
        }//fin if
        Alumno alumno = sistema.getAlumnos().get(fila);
        vista.setFormularioAlumno(
                alumno.getNombre(),
                alumno.getCorreo(),
                alumno.getPassword(),
                alumno.getMatricula(),
                alumno.isInscripcionPermitida());
    }

    public void cargarTablaAlumnos() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nombre", "Correo", "Matricula", "Inscripcion"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Alumno a : sistema.getAlumnos()) {
            modelo.addRow(new Object[]{
                a.getNombre(), a.getCorreo(), a.getMatricula(),
                a.isInscripcionPermitida() ? "Permitida" : "Denegada"
            });
        }//fin for
        vista.setModeloAlumnos(modelo);
    }

    // ─── Profesores ───────────────────────────────────────────────────────────
    private void agregarProfesor() {
        String nombre = vista.getNombreProfesor();
        String correo = vista.getCorreoProfesor();
        String pass = vista.getPasswordProfesor();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            Dialogos.advertencia(vista, "Completa todos los campos.", "Campos vacios");
            return;
        }//fin if

        String errorPass = ValidadorPassword.validar(pass);
        if (errorPass != null) {
            Dialogos.advertencia(vista,
                    errorPass + "\n\n" + ValidadorPassword.obtenerRequisitos(),
                    "Contrasena insegura");
            return;
        }//fin if

        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                Dialogos.advertencia(vista, "Ya existe un profesor con ese correo.", "Duplicado");
                return;
            }//fin if
        }//fin gor

        sistema.getProfesores().add(new Profesor(nombre, correo, pass));
        sistema.guardarSistema();
        cargarTablaProfesores();
        vista.limpiarFormularioProfesor();
        Dialogos.info(vista, "Profesor agregado correctamente.", "Exito");
    }

    private void editarProfesor() {
        int fila = vista.getFilaProfesorSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un profesor para editar.", "Sin seleccion");
            return;
        }//fin if 

        String nombre = vista.getNombreProfesor();
        String correo = vista.getCorreoProfesor();
        String pass = vista.getPasswordProfesor();

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            Dialogos.advertencia(vista, "Completa todos los campos.", "Campos vacios");
            return;
        }//fin if 

        String errorPass = ValidadorPassword.validar(pass);
        if (errorPass != null) {
            Dialogos.advertencia(vista,
                    errorPass + "\n\n" + ValidadorPassword.obtenerRequisitos(),
                    "Contrasena insegura");
            return;
        }//fin if 

        Profesor profesor = sistema.getProfesores().get(fila);
        profesor.setNombre(nombre);
        profesor.setCorreo(correo);
        profesor.setPassword(pass);
        sistema.guardarSistema();
        cargarTablaProfesores();
        vista.limpiarFormularioProfesor();
        Dialogos.info(vista, "Profesor actualizado correctamente.", "Exito");
    }

    private void eliminarProfesor() {
        int fila = vista.getFilaProfesorSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un profesor para eliminar.", "Sin seleccion");
            return;
        }//fin if

        Profesor profesor = sistema.getProfesores().get(fila);
        boolean confirma = Dialogos.confirmarPeligro(vista,
                "Eliminar al profesor \"" + profesor.getNombre() + "\"?\nEsta accion no se puede deshacer.",
                "Confirmar eliminacion");

        if (confirma) {
            sistema.getProfesores().remove(fila);
            sistema.guardarSistema();
            cargarTablaProfesores();
            vista.limpiarFormularioProfesor();
            Dialogos.info(vista, "Profesor eliminado.", "Eliminado");
        }//fin if 
    }

    private void cargarProfesorEnFormulario() {
        int fila = vista.getFilaProfesorSeleccionada();
        if (fila < 0) {
            return;
        }//fin if 
        Profesor profesor = sistema.getProfesores().get(fila);
        vista.setFormularioProfesor(
                profesor.getNombre(),
                profesor.getCorreo(),
                profesor.getPassword());
    }

    public void cargarTablaProfesores() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nombre", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Profesor p : sistema.getProfesores()) {
            modelo.addRow(new Object[]{p.getNombre(), p.getCorreo()});
        }//fin for 
        vista.setModeloProfesores(modelo);
    }

    // Materias y horiarios para la organizacion del sistema 
    private void agregarMateria() {
        String nombre = vista.getNombreMateria();
        String clave = vista.getClaveMateria();
        if (nombre.isEmpty() || clave.isEmpty()) {
            Dialogos.advertencia(vista, "Campos vacios.", "Campos vacios");
            return;
        }//fin if
        sistema.getMaterias().add(new Materia(nombre, clave));
        sistema.guardarSistema();
        cargarTablaMaterias();
        Dialogos.info(vista, "Materia agregada correctamente.", "Exito");
    }

    private void establecerHorario() {
        String clave = vista.getClaveMateriaHorario();
        String dia = vista.getDiaHorario();
        String hora = vista.getHoraHorario();
        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                m.setHorario(new Horario(dia, hora));
                sistema.guardarSistema();
                cargarTablaMaterias();
                Dialogos.info(vista, "Horario establecido para la materia \"" + m.getNombre() + "\".", "Exito");
                return;
            }//fin if 
        }//fin for
        Dialogos.error(vista, "Materia con clave \"" + clave + "\" no encontrada.", "No encontrada");
    }

    private void asignarMateriaAProfesor() {
        String clave = vista.getClaveMateriaHorario();
        String correo = vista.getCorreoProfesorAsignar();
        Materia mat = null;
        Profesor prof = null;

        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                mat = m;
            }//fin if  
        }//fin for 
        for (Profesor p : sistema.getProfesores()) {
            if (p.getCorreo().equalsIgnoreCase(correo)) {
                prof = p;
            }//fin if 
        }//fin for

        if (mat == null || prof == null) {
            Dialogos.error(vista, "Datos incorrectos. Verifica clave y correo.", "Error");
            return;
        }//fin if 
        for (Materia m : prof.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                Dialogos.advertencia(vista, "Esa materia ya esta asignada a ese profesor.", "Duplicado");
                return;
            }//fin if 
        }//fin for 
        prof.getMaterias().add(mat);
        sistema.guardarSistema();
        Dialogos.info(vista, "Materia asignada al profesor correctamente.", "Exito");
    }

    public void cargarTablaMaterias() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nombre", "Clave", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : sistema.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora()
                    : "Sin horario";
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), h});
        }//fin for 
        vista.setModeloMaterias(modelo);
    }

    // ─── Reportes ─────────────────────────────────────────────────────────────
    private void generarBoletinAlumnoSeleccionado() {
        int fila = vista.getFilaAlumnoSeleccionada();
        if (fila < 0) {
            Dialogos.advertencia(vista, "Selecciona un alumno de la tabla primero.", "Sin seleccion");
            return;
        }//fin if 
        String matricula = vista.getMatriculaAlumnoEnFila(fila);
        Alumno seleccionado = null;
        for (Alumno a : sistema.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                seleccionado = a;
                break;
            }//fin if 
        }//fin for 
        if (seleccionado == null) {
            return;
        }//fin if 
        ServicioReportes.generarBoletinAsync(seleccionado, vista.getBtnGenerarBoletinAdmin());
    }

    private void generarTodosLosReportes() {
        boolean confirma = Dialogos.confirmar(vista,
                "Se generaran boletines para " + sistema.getAlumnos().size()
                + " alumnos de forma paralela.\n¿Continuar?",
                "Generacion en lote");
        if (confirma) {
            ServicioReportes.generarReportesLoteAsync(sistema, vista.getBtnReportesLote());
        }//fin if 
    }

    // Para cerrar la sesion 
    private void cerrarSesion() {
        boolean confirma = Dialogos.confirmar(vista, "¿Deseas cerrar sesion?", "Cerrar Sesion");
        if (confirma) {
            VentanaLogin vLogin = new VentanaLogin();
            new ControladorLogin(vLogin, sistema);
            vLogin.setVisible(true);
            vista.dispose();
        }//fin if 
    }
}//fin de la clase 
