package itz.dao;

import itz.config.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CalificacionDAO {

    // ─── Alumno: obtener su kardex completo ──────────────────────────────────
    public List<Object[]> obtenerKardex(String matricula) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT nombre_materia, valor, fecha_registro, promedio_general "
                   + "FROM v_kardex WHERE matricula = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("nombre_materia"),
                    rs.getDouble("valor"),
                    rs.getDate("fecha_registro"),
                    rs.getDouble("promedio_general")
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar kardex: " + e.getMessage());
        }
        return lista;
    }

    // ─── Profesor: obtener alumnos inscritos en una materia con su calificacion ─
    public List<Object[]> obtenerAlumnosPorMateria(String claveMateria) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT a.matricula, u.nombre, "
                   + "COALESCE(c.valor, -1) AS calificacion "
                   + "FROM inscripcion i "
                   + "JOIN alumno a ON i.matricula = a.matricula "
                   + "JOIN usuario u ON a.usuario_id = u.id "
                   + "LEFT JOIN calificacion c "
                   + "  ON c.matricula = a.matricula AND c.clave_materia = i.clave_materia "
                   + "WHERE i.clave_materia = ? "
                   + "ORDER BY u.nombre";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, claveMateria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double cal = rs.getDouble("calificacion");
                lista.add(new Object[]{
                    rs.getString("matricula"),
                    rs.getString("nombre"),
                    // -1 significa que aun no tiene calificacion
                    cal < 0 ? "Sin calificacion" : String.format("%.2f", cal)
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar alumnos por materia: " + e.getMessage());
        }
        return lista;
    }

    // ─── Profesor: guardar o actualizar calificacion ─────────────────────────
    // Usa INSERT ... ON DUPLICATE KEY UPDATE para manejar ambos casos con una sola llamada.
    public boolean guardarOActualizarCalificacion(String matricula,
                                                   String claveMateria,
                                                   double valor) {
        // Validar rango antes de tocar la BD
        if (valor < 0 || valor > 100) return false;

        String sql = "INSERT INTO calificacion (matricula, clave_materia, valor) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE valor = VALUES(valor), "
                   + "fecha_registro = CURRENT_DATE";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ps.setString(2, claveMateria);
            ps.setDouble(3, valor);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al guardar calificacion: " + e.getMessage());
            return false;
        }
    }
}
