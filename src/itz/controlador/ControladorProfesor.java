package itz.controlador;

import itz.modelo.*;
import itz.util.*;
import itz.vista.*;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedHashMap;
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

    private void init() {
        cargarMateriasCombo();
        cargarTablaHorario();
        cargarTablaMisMaterias();
        cargarTablaAlumnosProfesor();

        vista.addComboListener(e -> cargarAlumnosMateria());
        vista.getBtnGuardarCalificacion().addActionListener(e -> guardarCalificacion());
        vista.getBtnRefrescarAlumnos().addActionListener(e -> cargarTablaAlumnosProfesor());
        vista.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void cargarMateriasCombo() {
        vista.removeAllItemsCombo();
        for (Materia m : profesor.getMaterias()) {
            vista.addItemCombo(m.getNombre());
        }//fin for 
    }

    private void cargarTablaHorario() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Clave", "Materia", "Horario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : profesor.getMaterias()) {
            String h = (m.getHorario() != null)
                    ? m.getHorario().getDia() + " " + m.getHorario().getHora() : "N/A";
            modelo.addRow(new Object[]{m.getClave(), m.getNombre(), h});
        }//fin for 
        vista.setModeloHorario(modelo);
    }

    private void cargarTablaMisMaterias() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Materia", "Clave", "Dia", "Hora", "Alumnos inscritos"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Materia m : profesor.getMaterias()) {
            String dia = (m.getHorario() != null) ? m.getHorario().getDia() : "-";
            String hora = (m.getHorario() != null) ? m.getHorario().getHora() : "-";
            int num = m.getAlumnos() != null ? m.getAlumnos().size() : 0;
            modelo.addRow(new Object[]{m.getNombre(), m.getClave(), dia, hora, num});
        }//fin for 
        vista.setModeloMisMaterias(modelo);
    }

    private void cargarAlumnosMateria() {
        int idx = vista.getComboMateriasSelectedIndex();
        if (idx < 0) {
            return;
        }//fin if 

        Materia seleccionada = profesor.getMaterias().get(idx);
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Matricula", "Nombre", "Calificacion Actual"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Alumno a : seleccionada.getAlumnos()) {
            double cal = obtenerCalificacionAlumno(a, seleccionada);
            String calStr = cal == -1 ? "Sin calificar" : String.format("%.1f", cal);
            modelo.addRow(new Object[]{a.getMatricula(), a.getNombre(), calStr});
        }//fin for 
        vista.setModeloAlumnos(modelo);
    }

    private double obtenerCalificacionAlumno(Alumno alumno, Materia materia) {
        for (Calificacion c : alumno.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                return c.getValor();
            }//fin if
        }//fin for 
        return -1;
    }

    public void cargarTablaAlumnosProfesor() {
        Map<String, String[]> mapaAlumnos = new LinkedHashMap<>();

        for (Materia m : profesor.getMaterias()) {
            for (Alumno a : m.getAlumnos()) {
                String key = a.getMatricula();
                if (mapaAlumnos.containsKey(key)) {
                    mapaAlumnos.get(key)[3] += ", " + m.getNombre();
                } else {
                    double cal = obtenerCalificacionAlumno(a, m);
                    String calStr = cal == -1 ? "-" : String.format("%.1f", cal);
                    mapaAlumnos.put(key, new String[]{
                        a.getNombre(), a.getMatricula(), a.getCorreo(),
                        m.getNombre(), calStr
                    });
                }//fin if-else
            }//fin for 
        }//fin for 

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nombre", "Matricula", "Correo", "Materia(s)", "Calificacion"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (String[] fila : mapaAlumnos.values()) {
            modelo.addRow(fila);
        }//fin for 
        vista.setModeloAlumnosProfesor(modelo);
    }

    private void guardarCalificacion() {
        int idx = vista.getComboMateriasSelectedIndex();
        if (idx < 0) {
            Dialogos.advertencia(vista, "Selecciona una materia primero.", "Sin seleccion");
            return;
        }//fin if 

        String matricula = vista.getMatriculaAlumno();
        String calStr = vista.getCalificacion();

        if (matricula.isEmpty() || calStr.isEmpty()) {
            Dialogos.advertencia(vista,
                    "Ingresa la matricula del alumno y la calificacion.", "Campos vacios");
            return;
        }//fin if 

        double calValor;
        //Manejo de excepciones (calificacion erronea)
        try {
            calValor = Double.parseDouble(calStr);
            if (calValor < 0 || calValor > 100) {
                Dialogos.advertencia(vista, "La calificacion debe estar entre 0 y 100.", "Valor invalido");
                return;
            }//fin if 
        } catch (NumberFormatException ex) {
            Dialogos.error(vista, "Calificacion invalida. Usa un numero (ej: 8.5)", "Error de formato");
            return;
        }//fin try-catch

        Materia materia = profesor.getMaterias().get(idx);
        Alumno alumnoTarget = null;
        for (Alumno a : materia.getAlumnos()) {
            if (a.getMatricula().equalsIgnoreCase(matricula)) {
                alumnoTarget = a;
                break;
            }//fin if 
        }//fin for 

        if (alumnoTarget == null) {
            Dialogos.error(vista,
                    "El alumno con matricula \"" + matricula + "\" no esta inscrito en esta materia.",
                    "No encontrado");
            return;
        }//fin if 

        boolean actualizado = false;
        for (Calificacion c : alumnoTarget.getKardex().getHistorial()) {
            if (c.getMateria().getClave().equalsIgnoreCase(materia.getClave())) {
                c.setValor(calValor);
                actualizado = true;
                break;
            }//fin if
        }//fin for 
        if (!actualizado) {
            alumnoTarget.getKardex().agregarCalificacion(new Calificacion(materia, calValor));
        }//fin if 

        sistema.guardarSistema();
        cargarAlumnosMateria();
        cargarTablaAlumnosProfesor();
        vista.limpiarCamposCalificacion();
        Dialogos.info(vista,
                "Calificacion " + calValor + " guardada para " + alumnoTarget.getNombre() + ".",
                "Guardado");
    }

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
