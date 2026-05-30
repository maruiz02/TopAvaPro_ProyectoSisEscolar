package itz.controlador;

import itz.config.ConexionDB;
import itz.dao.AlumnoDAO;
import itz.dao.ProfesorDAO;
import itz.modelo.Alumno;
import itz.modelo.Profesor;
import itz.vista.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControladorLogin {

    private VentanaLogin vista;

    public ControladorLogin(VentanaLogin vista) {
        this.vista = vista;
        vista.addLoginListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correo   = vista.getTxtCorreo().getText().trim();
        String password = new String(vista.getTxtPassword().getPassword()).trim();

        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }

        try (Connection conn = ConexionDB.conectar()) {

            String sql = "SELECT * FROM usuario WHERE correo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(vista, "Usuario no encontrado.");
                return;
            }

            String passwordBD = rs.getString("password_hash");
            String tipo       = rs.getString("tipo");
            String nombre     = rs.getString("nombre");

            if (!password.equals(passwordBD)) {
                JOptionPane.showMessageDialog(vista, "Contraseña incorrecta.");
                return;
            }

            switch (tipo) {

                case "administrador": {
                    VentanaAdmin v = new VentanaAdmin(nombre, correo);
                    new ControladorAdmin(v);
                    v.setVisible(true);
                    vista.dispose();
                    break;
                }

                case "profesor": {
                    // Cargar modelo Profesor completo desde BD
                    ProfesorDAO profesorDAO = new ProfesorDAO();
                    Profesor profesor = profesorDAO.buscarPorCorreo(correo);
                    if (profesor == null) {
                        JOptionPane.showMessageDialog(vista, "Error al cargar datos del profesor.");
                        return;
                    }
                    VentanaProfesor v = new VentanaProfesor(nombre, correo);
                    new ControladorProfesor(v, profesor);
                    v.setVisible(true);
                    vista.dispose();
                    break;
                }

                case "alumno": {
                    // Cargar modelo Alumno completo desde BD
                    AlumnoDAO alumnoDAO = new AlumnoDAO();
                    Alumno alumno = alumnoDAO.buscarPorCorreo(correo);
                    if (alumno == null) {
                        JOptionPane.showMessageDialog(vista, "Error al cargar datos del alumno.");
                        return;
                    }
                    VentanaAlumno v = new VentanaAlumno(alumno.getNombre(), alumno.getMatricula());
                    new ControladorAlumno(v, alumno);
                    v.setVisible(true);
                    vista.dispose();
                    break;
                }

                default:
                    JOptionPane.showMessageDialog(vista, "Tipo de usuario desconocido.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
