package itz.dao;

import itz.config.ConexionDB;
import itz.modelo.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    // ─── CRUD existente ───────────────────────────────────────────────────────

    public boolean insertarProfesor(Profesor profesor) {
        String sqlUsuario = "INSERT INTO usuario (nombre, correo, password_hash, tipo) "
                          + "VALUES (?, ?, ?, 'profesor')";
        String sqlProfesor = "INSERT INTO profesor(usuario_id) VALUES (?)";

        try (Connection con = ConexionDB.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement psU = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psP = con.prepareStatement(sqlProfesor)) {

                psU.setString(1, profesor.getNombre());
                psU.setString(2, profesor.getCorreo());
                psU.setString(3, profesor.getPassword());
                psU.executeUpdate();

                ResultSet rs = psU.getGeneratedKeys();
                if (!rs.next()) { con.rollback(); return false; }

                psP.setInt(1, rs.getInt(1));
                boolean ok = psP.executeUpdate() > 0;
                con.commit();
                return ok;

            } catch (SQLException ex) {
                con.rollback();
                System.out.println("Error insertar profesor: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error conexion insertar profesor: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Profesor> listarProfesores() {
        ArrayList<Profesor> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.correo, u.password_hash "
                   + "FROM profesor p INNER JOIN usuario u ON p.usuario_id = u.id";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Profesor p = new Profesor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCorreo(rs.getString("correo"));
                p.setPassword(rs.getString("password_hash"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error listar profesores: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarProfesor(int usuarioId) {
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement("DELETE FROM usuario WHERE id=?")) {
            ps.setInt(1, usuarioId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar profesor: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarProfesor(Profesor profesor) {
        String sql = "UPDATE usuario SET nombre=?, correo=?, password_hash=? WHERE id=?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getCorreo());
            ps.setString(3, profesor.getPassword());
            ps.setInt(4, profesor.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar profesor: " + e.getMessage());
            return false;
        }
    }

    // ─── NUEVOS: datos del profesor para su panel ─────────────────────────────

    /**
     * Devuelve el profesor_id interno de la tabla `profesor` a partir del usuario_id.
     * Se necesita para consultar profesor_materia.
     */
    public int obtenerProfesorId(int usuarioId) {
        String sql = "SELECT id FROM profesor WHERE usuario_id = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) {
            System.out.println("Error obtener profesor_id: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Materias asignadas al profesor: [clave, nombre, dia, hora]
     */
    public List<Object[]> obtenerMateriasPorProfesor(int profesorId) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT m.clave, m.nombre, m.dia, m.hora "
                   + "FROM profesor_materia pm "
                   + "JOIN materia m ON pm.clave_materia = m.clave "
                   + "WHERE pm.profesor_id = ? "
                   + "ORDER BY m.dia, m.hora";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, profesorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("clave"),
                    rs.getString("nombre"),
                    rs.getString("dia") != null ? rs.getString("dia") : "Por asignar",
                    rs.getString("hora") != null ? rs.getString("hora") : "--"
                });
            }
        } catch (Exception e) {
            System.out.println("Error obtener materias profesor: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un profesor por correo para el login.
     * Devuelve: [usuario_id, profesor_id, nombre, correo, password_hash]
     */
    public Profesor buscarPorCorreo(String correo) {
        String sql = "SELECT u.id, u.nombre, u.correo, u.password_hash "
                   + "FROM usuario u JOIN profesor p ON p.usuario_id = u.id "
                   + "WHERE u.correo = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Profesor p = new Profesor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setCorreo(rs.getString("correo"));
                p.setPassword(rs.getString("password_hash"));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Error buscar profesor por correo: " + e.getMessage());
        }
        return null;
    }
}
