package itz.controlador;

import itz.dao.CalificacionDAO;
import itz.dao.MateriaDAO;
import itz.modelo.Alumno;
import itz.vista.VentanaAlumno;
import itz.vista.VentanaLogin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.util.List;

public class ControladorAlumno {

    private VentanaAlumno vista;
    private Alumno alumno;
    private CalificacionDAO calificacionDAO;
    private MateriaDAO materiaDAO;

    public ControladorAlumno(VentanaAlumno vista, Alumno alumno) {
        this.vista = vista;
        this.alumno = alumno;
        this.calificacionDAO = new CalificacionDAO();
        this.materiaDAO = new MateriaDAO();

        // 1. Evaluar si tiene permiso de inscripción (Viene del login/DB)
        verificarPermisoInscripcion();

        // 2. Cargar todas las tablas con datos reales de la DB
        cargarTablaCalificaciones();
        cargarTablaHorario();
        cargarMateriasDisponibles();
        cargarMateriasInscritas();

        // 3. Configurar panel de reportes
        vista.getPanelReportes().configurar(alumno);

        // 4. Asignar acciones a los botones
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        vista.getBtnInscribir().addActionListener(e -> inscribirMateria());
        vista.getBtnCancelarInscripcion().addActionListener(e -> cancelarInscripcion());
    }

    private void verificarPermisoInscripcion() {
        if (alumno.isInscripcionPermitida()) {
            vista.setEstadoInscripcion("Estado: Inscripción Habilitada", new Color(46, 204, 113));
            vista.setBotonesInscripcionHabilitados(true);
        } else {
            vista.setEstadoInscripcion("Estado: Inscripción Denegada", new Color(231, 76, 60));
            vista.setBotonesInscripcionHabilitados(false);
        }
    }

    private void cargarTablaCalificaciones() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Materia", "Calificación (0-100)", "Fecha de Registro"}, 0);

        List<Object[]> kardex = calificacionDAO.obtenerKardex(alumno.getMatricula());
        double promedioGeneral = 0.0;

        for (Object[] fila : kardex) {
            // La fila contiene: [nombre_materia, valor, fecha_registro, promedio_general]
            modelo.addRow(new Object[]{fila[0], fila[1], fila[2]});
            promedioGeneral = (Double) fila[3]; // El promedio viene precalculado de la vista v_kardex
        }

        vista.setModeloCalificaciones(modelo);
        vista.setPromedio(String.format("Promedio General: %.2f", promedioGeneral));
    }

    private void cargarTablaHorario() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Día", "Hora"}, 0);
        
        List<Object[]> horario = materiaDAO.obtenerHorario(alumno.getMatricula());
        for (Object[] fila : horario) {
            modelo.addRow(fila);
        }
        vista.setModeloHorario(modelo);
    }

    private void cargarMateriasDisponibles() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Horario"}, 0);
        
        List<Object[]> disponibles = materiaDAO.obtenerMateriasDisponibles(alumno.getMatricula());
        for (Object[] fila : disponibles) {
            modelo.addRow(fila);
        }
        vista.setModeloMateriasDisponibles(modelo);
    }

    private void cargarMateriasInscritas() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Horario"}, 0);
        
        List<Object[]> inscritas = materiaDAO.obtenerMateriasInscritasParaTabla(alumno.getMatricula());
        for (Object[] fila : inscritas) {
            modelo.addRow(fila);
        }
        vista.setModeloMateriasInscritas(modelo);
    }

    private void inscribirMateria() {
        String clave = vista.getClaveMateriaSelecionadaDisponible();
        if (clave != null) {
            boolean exito = materiaDAO.inscribir(alumno.getMatricula(), clave);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Materia inscrita correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Refrescar las tablas
                cargarMateriasDisponibles();
                cargarMateriasInscritas();
                cargarTablaHorario();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al inscribir la materia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione una materia disponible.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarInscripcion() {
        String clave = vista.getClaveMateriaSelecionadaInscrita();
        if (clave != null) {
            boolean exito = materiaDAO.cancelarInscripcion(alumno.getMatricula(), clave);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Inscripción cancelada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Refrescar las tablas
                cargarMateriasDisponibles();
                cargarMateriasInscritas();
                cargarTablaHorario();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al cancelar la inscripción.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione una materia inscrita.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cerrarSesion() {
        VentanaLogin vLogin = new VentanaLogin();
        new ControladorLogin(vLogin); // Asumiendo que tienes este controlador
        vLogin.setVisible(true);
        vista.dispose();
    }
}