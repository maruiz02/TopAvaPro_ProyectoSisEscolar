package itz.controlador;

import itz.modelo.*;
import itz.vista.VentanaLogin;
import itz.vista.VentanaProfesor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ControladorProfesor {

    //Declaracion de variables
    private VentanaProfesor vista;
    private SistemaEscolar sistema;
    private Profesor profesor;

    public ControladorProfesor(VentanaProfesor vista, SistemaEscolar sistema, Profesor profesor) {
        this.vista = vista;
        this.sistema = sistema;
        this.profesor = profesor;
        init();
    }

    //Visualizar materias y horarios
    private void init() {
        cargarMateriasCombo();
        cargarTablaHorario();
        cargarTablaMisMaterias();
        cargarTablaAlumnosProfesor();
        vista.comboMaterias.addActionListener(e -> cargarAlumnosMateria());
        vista.btnGuardarCalificacion.addActionListener(e -> guardarCalificacion());
        vista.btnRefrescarAlumnos.addActionListener(e -> cargarTablaAlumnosProfesor());
        vista.btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    //Cargar las materias     
    private void cargarMateriasCombo() {
        vista.comboMaterias.removeAllItems();
        for (Materia m : profesor.getMaterias()) {
            vista.comboMaterias.addItem(m.getNombre());
        }//Fin for
    }

    //Cargando horario
    private void cargarTablaHorario() {
        String[] col = {"Clave", "Materia", "Horario"};
        DefaultTableModel modelo = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : profesor.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "N/A";
            modelo.addRow(new Object[]{m.getClave(), m.getNombre(), h});
        }//Fin for
        vista.tablaHorario.setModel(modelo);
    }

    //Cargar materias para el profesor 
    private void cargarTablaMisMaterias() {
        String[] columnas = {"Materia", "Clave", "Día", "Hora", "Alumnos inscritos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : profesor.getMaterias()) {
            String dia = (m.getHorario() != null) ? m.getHorario().getDia() : "—";
            String hora = (m.getHorario() != null) ? m.getHorario().getHora() : "—";
            int num = m.getAlumnos() != null ? m.getAlumnos().size() : 0;
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), dia, hora, num});
        }//Fin for
        vista.tablaMissMaterias.setModel(modelo);
    }

    //Cargar los alumnos asignados por materia
    private void cargarAlumnosMateria() {
        int idx = vista.comboMaterias.getSelectedIndex();
        if (idx < 0) {
            return;
        }//Fin if 

        Materia seleccionada = profesor.getMaterias().get(idx);
        String[] cols = {"Matrícula", "Nombre", "Calificación Actual"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Alumno a : seleccionada.getAlumnos()) {
            double cal = obtenerCalificacionAlumno(a, seleccionada);
            String calStr = cal == -1 ? "Sin calificar" : String.format("%.1f", cal);
            modelo.addRow(new Object[]{a.getMatricula(), a.getNombre(), calStr});
        }//Fin for 
        vista.tablaAlumnos.setModel(modelo);
    }

    private double obtenerCalificacionAlumno(Alumno alumno, Materia materia) {
        for (Calificacion c : alumno.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                return c.getValor();
            }//Fin if 
        }//Fin for 
        return -1;
    }

    /**
     * Carga en tablaAlumnosProfesor TODOS los alumnos inscritos en
     * cualquiera de las materias que imparte este profesor.
     * Usa un LinkedHashMap para evitar duplicados, manteniendo el orden
     * de inserción y agrupando las materias de cada alumno.
     */
    public void cargarTablaAlumnosProfesor() {
        // Mapa: matrícula -> fila de datos ya construida
        // Si un alumno aparece en varias materias se acumulan las materias.
        Map<String, String[]> mapaAlumnos = new LinkedHashMap<>();

        for (Materia m : profesor.getMaterias()) {
            for (Alumno a : m.getAlumnos()) {
                String key = a.getMatricula();
                if (mapaAlumnos.containsKey(key)) {
                    // Agregar esta materia a la lista ya existente
                    String materiasYa = mapaAlumnos.get(key)[3];
                    mapaAlumnos.get(key)[3] = materiasYa + ", " + m.getNombre();
                } else {
                    double cal = obtenerCalificacionAlumno(a, m);
                    String calStr = cal == -1 ? "—" : String.format("%.1f", cal);
                    mapaAlumnos.put(key, new String[]{
                        a.getNombre(),
                        a.getMatricula(),
                        a.getCorreo(),
                        m.getNombre(),
                        calStr
                    });
                }//Fin if-else
            }//Fin for alumnos
        }//Fin for materias

        String[] columnas = {"Nombre", "Matrícula", "Correo", "Materia(s)", "Calificación"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (String[] fila : mapaAlumnos.values()) {
            modelo.addRow(fila);
        }//Fin for

        vista.tablaAlumnosProfesor.setModel(modelo);
    }//Fin cargarTablaAlumnosProfesor

    //Guardando la calificacion 
    private void guardarCalificacion() {
        int idx = vista.comboMaterias.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(vista, "Selecciona una materia primero.");
            return;
        }//Fin if 

        String matricula = vista.txtIdAlumno.getText().trim();
        String calStr = vista.txtCalificacion.getText().trim();

        if (matricula.isEmpty() || calStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingresa la matrícula del alumno y la calificación.");
            return;
        }//Fin if 

        double calValor;
        try {
            calValor = Double.parseDouble(calStr);
            if (calValor < 0 || calValor > 100) {
                JOptionPane.showMessageDialog(vista, "La calificación debe estar entre 0 y 100.");
                return;
            }//Fin if 
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Calificación inválida. Usa un número (ej: 8.5)");
            return;
        }

        Materia materia = profesor.getMaterias().get(idx);

        Alumno alumnoTarget = null;
        for (Alumno a : materia.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                alumnoTarget = a;
                break;
            }
        }

        if (alumnoTarget == null) {
            JOptionPane.showMessageDialog(vista,
                    "El alumno con matrícula \"" + matricula + "\" no está inscrito en esta materia.");
            return;
        }

        // Actualizar calificacion si ya existe, agregar si no
        boolean actualizado = false;
        for (Calificacion c : alumnoTarget.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                c.setValor(calValor);
                actualizado = true;
                break;
            }//Fin if 
        }//Fin for
        if (!actualizado) {
            alumnoTarget.getKardex().agregarCalificacion(new Calificacion(materia, calValor));
        }//Fin ir 

        sistema.guardarSistema();
        cargarAlumnosMateria();
        cargarTablaAlumnosProfesor();
        vista.txtIdAlumno.setText("");
        vista.txtCalificacion.setText("");
        JOptionPane.showMessageDialog(vista,
                "Calificación " + calValor + " guardada para " + alumnoTarget.getNombre() + ".");
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