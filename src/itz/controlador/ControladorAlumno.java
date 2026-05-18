package itz.controlador;

import itz.modelo.*;
import itz.util.*;
import itz.vista.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class ControladorAlumno {

    //Declaracion de las variables 
    private VentanaAlumno vista;
    private SistemaEscolar sistema;
    private Alumno alumno;

    public ControladorAlumno(VentanaAlumno vista, SistemaEscolar sistema, Alumno alumnoLogin) {
        this.vista = vista;
        this.sistema = sistema;
        this.alumno = buscarAlumnoActualizado(alumnoLogin.getMatricula());
        if (this.alumno == null) {
            this.alumno = alumnoLogin;
        }//fin if 

        cargarCalificaciones();
        cargarHorario();
        cargarInscripcion();
        actualizarEstadoInscripcion();

        vista.getBtnInscribir().addActionListener(e -> inscribirMateria());
        vista.getBtnCancelarInscripcion().addActionListener(e -> cancelarInscripcion());
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private Alumno buscarAlumnoActualizado(String matricula) {
        SistemaEscolar sistemaFresco = SistemaEscolar.cargarSistema();
        for (Alumno a : sistemaFresco.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                sistema = sistemaFresco;
                return a;
            }//fin if
        }//fin for 
        return null;
    }

    // Calificaciones
    private void cargarCalificaciones() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Materia", "Clave", "Calificacion", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : alumno.getMaterias()) {
            double cal = obtenerCalificacion(m);
            String estado = cal == -1 ? "Sin calificar" : (cal >= 6.0 ? "Aprobado" : "Reprobado");
            String calStr = cal == -1 ? "-" : String.format("%.1f", cal);
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), calStr, estado});
        }//fin for
        vista.setModeloCalificaciones(modelo);
        mostrarPromedio();
    }

    private double obtenerCalificacion(Materia materia) {
        for (Calificacion c : alumno.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                return c.getValor();
            }//fin if 
        }//fin for 
        return -1;
    }

    private void mostrarPromedio() {
        double promedio = alumno.getKardex().calcularPromedio();
        vista.setPromedio(String.format("Promedio General: %.2f", promedio));
    }

    // ─── Horario ──────────────────────────────────────────────────────────────
    private void cargarHorario() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Materia", "Clave", "Dia", "Hora"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : alumno.getMaterias()) {
            String dia = (m.getHorario() != null) ? m.getHorario().getDia() : "-";
            String hora = (m.getHorario() != null) ? m.getHorario().getHora() : "-";
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), dia, hora});
        }//fin for 
        vista.setModeloHorario(modelo);
    }

    // Para la inscripcion 
    private void actualizarEstadoInscripcion() {
        Alumno fresco = recargarAlumno();
        if (fresco != null) {
            alumno = fresco;
        }//fin if 

        if (alumno.isInscripcionPermitida()) {
            vista.setEstadoInscripcion(
                    "Inscripcion PERMITIDA - puedes seleccionar materias",
                    new Color(46, 204, 113));
            vista.setBotonesInscripcionHabilitados(true);
        } else {
            vista.setEstadoInscripcion(
                    "Inscripcion Denegada - contacta a un administrador",
                    new Color(231, 76, 60));
            vista.setBotonesInscripcionHabilitados(false);
        }//fin if-else
    }

    private Alumno recargarAlumno() {
        SistemaEscolar sf = SistemaEscolar.cargarSistema();
        for (Alumno a : sf.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(alumno.getMatricula())) {
                sistema = sf;
                return a;
            }//fin if 
        }//fin for 
        return null;
    }

    private void cargarInscripcion() {
        // Materias disponibles
        DefaultTableModel modeloDisp = new DefaultTableModel(
                new String[]{"Materia", "Clave", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : sistema.getMaterias()) {
            if (!alumnoTieneMateria(m)) {
                String h = (m.getHorario() != null)
                        ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "-";
                modeloDisp.addRow(new Object[]{m.getNombre(), m.getClave(), h});
            }//fin if
        }//fin for 
        vista.setModeloMateriasDisponibles(modeloDisp);

        // Materias inscritas
        DefaultTableModel modeloInsc = new DefaultTableModel(
                new String[]{"Materia", "Clave", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : alumno.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "-";
            modeloInsc.addRow(new Object[]{m.getNombre(), m.getClave(), h});
        }//fin for 
        vista.setModeloMateriasInscritas(modeloInsc);
    }

    private boolean alumnoTieneMateria(Materia materia) {
        for (Materia m : alumno.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(materia.getClave())) {
                return true;
            }//fin if 
        }//fin for 
        return false;
    }

    private void inscribirMateria() {
        actualizarEstadoInscripcion();

        if (!alumno.isInscripcionPermitida()) {
            Dialogos.advertencia(vista,
                    "No tienes permiso para inscribirte.\nContacta al administrador.",
                    "Inscripcion denegada");
            return;
        }//fin if 

        String clave = vista.getClaveMateriaSelecionadaDisponible();
        if (clave == null) {
            Dialogos.advertencia(vista,
                    "Selecciona una materia disponible para inscribirte.", "Sin seleccion");
            return;
        }//fin if 

        Materia seleccionada = null;
        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                seleccionada = m;
                break;
            }//fin if 
        }//fin for 
        if (seleccionada == null) {
            return;
        }//fin if 

        alumno.getMaterias().add(seleccionada);
        if (!seleccionada.getAlumnos().contains(alumno)) {
            seleccionada.getAlumnos().add(alumno);
        }//fin if 

        sistema.guardarSistema();
        cargarInscripcion();
        cargarCalificaciones();
        cargarHorario();
        Dialogos.info(vista,
                "Inscrito correctamente en \"" + seleccionada.getNombre() + "\".",
                "Inscripcion exitosa");
    }

    private void cancelarInscripcion() {
        actualizarEstadoInscripcion();

        if (!alumno.isInscripcionPermitida()) {
            Dialogos.advertencia(vista,
                    "No tienes permiso para modificar tu inscripcion.", "Inscripcion denegada");
            return;
        }//fin if 

        String clave = vista.getClaveMateriaSelecionadaInscrita();
        if (clave == null) {
            Dialogos.advertencia(vista,
                    "Selecciona una materia inscrita para cancelarla.", "Sin seleccion");
            return;
        }//fin if 

        Materia aRemover = null;
        for (Materia m : alumno.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                aRemover = m;
                break;
            }//fin if
        }//fin for 
        if (aRemover == null) {
            return;
        }//fin if 

        final Materia materiaFinal = aRemover;
        boolean confirma = Dialogos.confirmarPeligro(vista,
                "Cancelar inscripcion en \"" + materiaFinal.getNombre() + "\"?\n"
                + "Perderan las calificaciones registradas en esa materia.",
                "Confirmar cancelacion");

        if (confirma) {
            alumno.getMaterias().remove(materiaFinal);
            materiaFinal.getAlumnos().remove(alumno);
            alumno.getKardex().getHistorial().removeIf(
                    c -> c.getMateria().getClave().equalsIgnoreCase(clave));
            sistema.guardarSistema();
            cargarInscripcion();
            cargarCalificaciones();
            cargarHorario();
            Dialogos.info(vista, "Inscripcion cancelada.", "Cancelado");
        }//fin if 
    }

    //Para cerrar la sesion 
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
