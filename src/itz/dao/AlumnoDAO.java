package itz.dao;

import itz.config.ConexionDB;
import itz.modelo.Alumno;

import java.sql.*;
import java.util.ArrayList;

public class AlumnoDAO {

    public boolean insertarAlumno(Alumno alumno) {
        String sqlUsuario = "INSERT INTO usuario (nombre, correo, password_hash, tipo) VALUES (?, ?, ?, 'alumno')";
        String sqlAlumno = "INSERT INTO alumno (matricula, usuario_id, inscripcion_permitida) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psAlumno = con.prepareStatement(sqlAlumno)) {

                psUsuario.setString(1, alumno.getNombre());
                psUsuario.setString(2, alumno.getCorreo());
                psUsuario.setString(3, alumno.getPassword());
                psUsuario.executeUpdate();

                ResultSet rs = psUsuario.getGeneratedKeys();
                if (!rs.next()) {
                    con.rollback();
                    return false;
                }

                int usuarioId = rs.getInt(1);

                psAlumno.setString(1, alumno.getMatricula());
                psAlumno.setInt(2, usuarioId);
                psAlumno.setBoolean(3, alumno.isInscripcionPermitida());

                boolean ok = psAlumno.executeUpdate() > 0;
                con.commit();
                return ok;

            } catch (SQLException ex) {
                con.rollback();
                System.out.println("Error insertar alumno: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error conexión insertar alumno: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Alumno> listarAlumnos() {
        ArrayList<Alumno> lista = new ArrayList<>();
        String sql = "SELECT u.id, u.nombre, u.correo, u.password_hash, a.matricula, a.inscripcion_permitida "
                   + "FROM usuario u LEFT JOIN alumno a ON a.usuario_id = u.id WHERE u.tipo='alumno'";

        try (Connection con = ConexionDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getInt("id"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setCorreo(rs.getString("correo"));
                alumno.setPassword(rs.getString("password_hash"));
                alumno.setMatricula(rs.getString("matricula"));
                alumno.setInscripcionPermitida(rs.getBoolean("inscripcion_permitida"));
                lista.add(alumno);
            }
        } catch (SQLException e) {
            System.out.println("Error listar alumnos: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarAlumno(int id) {
        String sqlAlumno = "DELETE FROM alumno WHERE usuario_id=?";
        String sqlUsuario = "DELETE FROM usuario WHERE id=?";

        try (Connection con = ConexionDB.conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement psAlumno = con.prepareStatement(sqlAlumno);
                 PreparedStatement psUsuario = con.prepareStatement(sqlUsuario)) {

                psAlumno.setInt(1, id);
                psAlumno.executeUpdate();

                psUsuario.setInt(1, id);
                boolean ok = psUsuario.executeUpdate() > 0;

                con.commit();
                return ok;

            } catch (SQLException ex) {
                con.rollback();
                System.out.println("Error eliminar alumno: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error conexión eliminar alumno: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarAlumno(Alumno alumno) {
        String sqlUsuario = "UPDATE usuario SET nombre=?, correo=?, password_hash=? WHERE id=?";
        String sqlAlumno = "UPDATE alumno SET matricula=? WHERE usuario_id=?";

        try (Connection con = ConexionDB.conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario);
                 PreparedStatement psAlumno = con.prepareStatement(sqlAlumno)) {

                psUsuario.setString(1, alumno.getNombre());
                psUsuario.setString(2, alumno.getCorreo());
                psUsuario.setString(3, alumno.getPassword());
                psUsuario.setInt(4, alumno.getId());
                psUsuario.executeUpdate();

                psAlumno.setString(1, alumno.getMatricula());
                psAlumno.setInt(2, alumno.getId());
                boolean ok = psAlumno.executeUpdate() > 0;

                con.commit();
                return ok;

            } catch (SQLException ex) {
                con.rollback();
                System.out.println("Error actualizar alumno: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error conexión actualizar alumno: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarPermisoInscripcion(String matricula, boolean permitido) {
        String sql = "UPDATE alumno SET inscripcion_permitida=? WHERE matricula=?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, permitido);
            ps.setString(2, matricula);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizar permiso: " + e.getMessage());
        }
        return false;
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT id FROM usuario WHERE correo=?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error verificar correo: " + e.getMessage());
        }
        return false;
    }

    public Alumno buscarPorCorreo(String correo) {
        String sql = "SELECT u.id, u.nombre, u.correo, u.password_hash, a.matricula, a.inscripcion_permitida "
                   + "FROM usuario u JOIN alumno a ON a.usuario_id = u.id WHERE u.correo = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Alumno alumno = new Alumno();
                    alumno.setId(rs.getInt("id"));
                    alumno.setNombre(rs.getString("nombre"));
                    alumno.setCorreo(rs.getString("correo"));
                    alumno.setPassword(rs.getString("password_hash"));
                    alumno.setMatricula(rs.getString("matricula"));
                    alumno.setInscripcionPermitida(rs.getBoolean("inscripcion_permitida"));
                    return alumno;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error buscar alumno por correo: " + e.getMessage());
        }
        return null;
    }
}