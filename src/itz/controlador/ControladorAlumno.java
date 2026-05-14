package itz.controlador;

import itz.modelo.*;
import itz.reporte.ServicioReportes;
import itz.vista.VentanaAlumno;
import itz.vista.VentanaLogin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class ControladorAlumno {

    //Declaracion de variables
    private VentanaAlumno vista;
    private SistemaEscolar sistema;
    private Alumno alumno;
    
    //Constructor 
    public ControladorAlumno(VentanaAlumno vista, SistemaEscolar sistema, Alumno alumnoLogin) {
        this.vista = vista;
        this.sistema = sistema;
        this.alumno = buscarAlumnoActualizado(alumnoLogin.getMatricula());
        if (this.alumno == null) {
            this.alumno = alumnoLogin;
        }//Fin if 

        cargarCalificaciones();
        cargarHorario();
        cargarInscripcion();
        actualizarEstadoInscripcion();

        vista.btnInscribir.addActionListener(e -> inscribirMateria());
        vista.btnCancelarInscripcion.addActionListener(e -> cancelarInscripcion());
        vista.btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    //Buscando alumno
    private Alumno buscarAlumnoActualizado(String matricula) {
        SistemaEscolar sistemaFresco = SistemaEscolar.cargarSistema();
        for (Alumno a : sistemaFresco.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                sistema = sistemaFresco;
                return a;
            }//Fin if
        }//Fin for
        return null;
    }

    // Parte de las calificaciones
    private void cargarCalificaciones() {
        String[] columnas = {"Materia", "Clave", "Calificación", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Materia m : alumno.getMaterias()) {
            double cal = obtenerCalificacion(m);
            String estado = cal == -1 ? "Sin calificar" : (cal >= 6.0 ? "✔ Aprobado" : "✘ Reprobado");
            String calStr = cal == -1 ? "—" : String.format("%.1f", cal);
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), calStr, estado});
        }//Fin for

        vista.tablaCalificaciones.setModel(modelo);
        mostrarPromedio();
    }

    //Metodo para obtener calificaciones
    private double obtenerCalificacion(Materia materia) {
        for (Calificacion c : alumno.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                return c.getValor();
            }//Fin ir
        }//Fin for
        return -1;
    }

    //Mostrando promedio del alumno
    private void mostrarPromedio() {
        double promedio = alumno.getKardex().calcularPromedio();
        vista.lblPromedio.setText(String.format("Promedio General: %.2f", promedio));
    }

    //Parte del horario
    private void cargarHorario() {
        String[] columnas = {"Materia", "Clave", "Día", "Hora"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Materia m : alumno.getMaterias()) {
            String dia = (m.getHorario() != null) ? m.getHorario().getDia() : "—";
            String hora = (m.getHorario() != null) ? m.getHorario().getHora() : "—";
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), dia, hora});
        }//Fin for

        vista.tablaHorario.setModel(modelo);
    }

    // Parte de la inscripcion
    private void actualizarEstadoInscripcion() {
        Alumno fresco = recargarAlumno();
        if (fresco != null) {
            alumno = fresco;
        }//Fin if 

        boolean permitida = alumno.isInscripcionPermitida();
        if (permitida) {
            vista.lblEstadoInscripcion.setText("Inscripción PERMITIDA — puedes seleccionar materias");
            vista.lblEstadoInscripcion.setForeground(new Color(46, 204, 113));
        } else {
            vista.lblEstadoInscripcion.setText("Inscripción Denegada — contacta a un administrador");
            vista.lblEstadoInscripcion.setForeground(new Color(231, 76, 60));
        }//Fin If
        vista.btnInscribir.setEnabled(permitida);
        vista.btnCancelarInscripcion.setEnabled(permitida);
    }

    private Alumno recargarAlumno() {
        SistemaEscolar sf = SistemaEscolar.cargarSistema();
        for (Alumno a : sf.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(alumno.getMatricula())) {
                sistema = sf;
                return a;
            }//Fin if 
        }//Fin for
        return null;
    }

    //Para cargar la inscripcion del alumnos
    private void cargarInscripcion() {
        // Materias disponibles 
        String[] colDisp = {"Materia", "Clave", "Horario"};
        DefaultTableModel modeloDisp = new DefaultTableModel(colDisp, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : sistema.getMaterias()) {
            if (!alumnoTieneMateria(m)) {
                String h = (m.getHorario() != null)
                        ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "—";
                modeloDisp.addRow(new Object[]{m.getNombre(), m.getClave(), h});
            }//Fin if
        }//Fin for
        vista.tablaMaterias.setModel(modeloDisp);

        // Materias inscritas
        String[] colInsc = {"Materia", "Clave", "Horario"};
        DefaultTableModel modeloInsc = new DefaultTableModel(colInsc, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : alumno.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "—";
            modeloInsc.addRow(new Object[]{m.getNombre(), m.getClave(), h});
        }//Fin for
        vista.tablaMateriasInscritas.setModel(modeloInsc);
    }

    //Para ver las materias asignadas al alumnos
    private boolean alumnoTieneMateria(Materia materia) {
        for (Materia m : alumno.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(materia.getClave())) {
                return true;
            }//Fin if
        }//Fin for
        return false;
    }

    //Metodo para verificar inscripcion autorizada
    private void inscribirMateria() {
        // Re-verificar permiso en tiempo real (por si el admin lo cambió desde otra sesión)
        actualizarEstadoInscripcion();

        if (!alumno.isInscripcionPermitida()) {
            JOptionPane.showMessageDialog(vista,
                    "No tienes permiso para inscribirte.\nContacta al administrador.");
            return;
        }//Fin if 

        int fila = vista.tablaMaterias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona una materia disponible para inscribirte.");
            return;
        }//Fin if

        String clave = (String) vista.tablaMaterias.getValueAt(fila, 1);
        Materia seleccionada = null;
        for (Materia m : sistema.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                seleccionada = m;
                break;
            }//Fin if
        }//Fin for
        if (seleccionada == null) {
            return;
        }//Fin if 

        alumno.getMaterias().add(seleccionada);
        if (!seleccionada.getAlumnos().contains(alumno)) {
            seleccionada.getAlumnos().add(alumno);
        }//Fin if 

        sistema.guardarSistema();
        cargarInscripcion();
        cargarCalificaciones();
        cargarHorario();
        JOptionPane.showMessageDialog(vista,
                "¡Inscrito correctamente en \"" + seleccionada.getNombre() + "\"!");
    }

    //Metodo para verficar inscripcion denegada
    private void cancelarInscripcion() {
        actualizarEstadoInscripcion();

        if (!alumno.isInscripcionPermitida()) {
            JOptionPane.showMessageDialog(vista, "No tienes permiso para modificar tu inscripción.");
            return;
        }//Fin if

        int fila = vista.tablaMateriasInscritas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona una materia inscrita para cancelarla.");
            return;
        }//Fin if 

        String clave = (String) vista.tablaMateriasInscritas.getValueAt(fila, 1);
        Materia aRemover = null;
        for (Materia m : alumno.getMaterias()) {
            if (m.getClave().equalsIgnoreCase(clave)) {
                aRemover = m;
                break;
            }//Fin if 
        }//Fin for
        if (aRemover == null) {
            return;
        }//Fin if 

        int confirm = JOptionPane.showConfirmDialog(vista,
                "¿Cancelar inscripción en \"" + aRemover.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            alumno.getMaterias().remove(aRemover);
            aRemover.getAlumnos().remove(alumno);
            alumno.getKardex().getHistorial().removeIf(
                    c -> c.getMateria().getClave().equalsIgnoreCase(clave));
            sistema.guardarSistema();
            cargarInscripcion();
            cargarCalificaciones();
            cargarHorario();
            JOptionPane.showMessageDialog(vista, "Inscripción cancelada.");
        }//Fin if
    }

    // Cerrar sesión y volver al login
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
            VentanaLogin vLogin = new VentanaLogin();
            new ControladorLogin(vLogin, sistema);
            vLogin.setVisible(true);
            vista.dispose();
        }//Fin if
    }//Fin cerrarSesion

}//Fin de la clase
