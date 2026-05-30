package itz.dao;

import itz.config.ConexionDB;
import itz.modelo.Materia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    // ─── Para el Admin: CRUD completo ────────────────────────────────────────

    public ArrayList<Materia> listarMaterias() {
        ArrayList<Materia> lista = new ArrayList<>();
        String sql = "SELECT clave, nombre, dia, hora FROM materia ORDER BY nombre";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Materia(
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    rs.getString("dia"),
                    rs.getString("hora")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error al listar materias: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertarMateria(Materia materia) {
        String sql = "INSERT INTO materia (clave, nombre, dia, hora) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, materia.getClave());
            ps.setString(2, materia.getNombre());
            // dia y hora pueden ser nulos si aun no se asignan
            ps.setString(3, materia.getDia().isEmpty()  ? null : materia.getDia());
            ps.setString(4, materia.getHora().isEmpty() ? null : materia.getHora());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar materia: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarMateria(Materia materia) {
        String sql = "UPDATE materia SET nombre=?, dia=?, hora=? WHERE clave=?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, materia.getNombre());
            ps.setString(2, materia.getDia().isEmpty()  ? null : materia.getDia());
            ps.setString(3, materia.getHora().isEmpty() ? null : materia.getHora());
            ps.setString(4, materia.getClave());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar materia: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarMateria(String clave) {
        // Las FK con ON DELETE CASCADE se encargan de inscripciones,
        // calificaciones y profesor_materia relacionadas.
        String sql = "DELETE FROM materia WHERE clave=?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, clave);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar materia: " + e.getMessage());
            return false;
        }
    }

    // ─── Para el Alumno: consultas por matricula ──────────────────────────────

    public List<Object[]> obtenerHorario(String matricula) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT m.clave, m.nombre, m.dia, m.hora FROM materia m "
                   + "JOIN inscripcion i ON m.clave = i.clave_materia "
                   + "WHERE i.matricula = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    rs.getString("dia")  != null ? rs.getString("dia")  : "Por asignar",
                    rs.getString("hora") != null ? rs.getString("hora") : "--"
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar horario: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> obtenerMateriasDisponibles(String matricula) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT clave, nombre, CONCAT(COALESCE(dia,''), ' ', COALESCE(hora,'')) AS horario "
                   + "FROM materia "
                   + "WHERE clave NOT IN (SELECT clave_materia FROM inscripcion WHERE matricula = ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String h = rs.getString("horario").trim();
                lista.add(new Object[]{
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    h.isEmpty() ? "Por asignar" : h
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar materias disponibles: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> obtenerMateriasInscritasParaTabla(String matricula) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT m.clave, m.nombre, "
                   + "CONCAT(COALESCE(m.dia,''), ' ', COALESCE(m.hora,'')) AS horario "
                   + "FROM materia m "
                   + "JOIN inscripcion i ON m.clave = i.clave_materia "
                   + "WHERE i.matricula = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String h = rs.getString("horario").trim();
                lista.add(new Object[]{
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    h.isEmpty() ? "Por asignar" : h
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar materias inscritas: " + e.getMessage());
        }
        return lista;
    }

    // ─── Inscripción / cancelación ────────────────────────────────────────────

    public boolean inscribir(String matricula, String claveMateria) {
        String sql = "INSERT INTO inscripcion (matricula, clave_materia) VALUES (?, ?)";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ps.setString(2, claveMateria);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error en inscripción: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelarInscripcion(String matricula, String claveMateria) {
        String sql = "DELETE FROM inscripcion WHERE matricula = ? AND clave_materia = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matricula);
            ps.setString(2, claveMateria);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al cancelar inscripción: " + e.getMessage());
            return false;
        }
    }
}
