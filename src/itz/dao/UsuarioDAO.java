package itz.dao;

import itz.config.ConexionDB;
import itz.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {

    public boolean insertarUsuario(Usuario usuario) {

        String sql =
        "INSERT INTO usuario(nombre, apellido, correo, password, tipo_usuario) VALUES(?,?,?,?,?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getTipoUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return false;
    }

    public ArrayList<Usuario> listarUsuarios() {

        ArrayList<Usuario> lista =
                new ArrayList<>();

        String sql = "SELECT * FROM usuario";

        try (Connection con = ConexionDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setNombre(
                        rs.getString("nombre")
                );

                usuario.setApellido(
                        rs.getString("apellido")
                );

                usuario.setCorreo(
                        rs.getString("correo")
                );

                usuario.setPassword(
                        rs.getString("password")
                );

                usuario.setTipoUsuario(
                        rs.getString("tipo_usuario")
                );

                lista.add(usuario);
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return lista;
    }
}