package itz.dao;

import itz.config.ConexionDB;
import itz.modelo.Administrador;

import java.sql.*;
import java.util.ArrayList;

public class AdministradorDAO {

    public boolean insertarAdministrador(
            Administrador admin
    ) {

        Connection conn = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psAdmin = null;
        ResultSet rs = null;

        try {

            conn = ConexionDB.conectar();

            // INSERT EN USUARIO
            String sqlUsuario =
                    "INSERT INTO usuario "
                    + "(nombre, correo, password_hash, tipo) "
                    + "VALUES (?, ?, ?, 'administrador')";

            psUsuario =
                    conn.prepareStatement(
                            sqlUsuario,
                            Statement.RETURN_GENERATED_KEYS
                    );

            psUsuario.setString(
                    1,
                    admin.getNombre()
            );

            psUsuario.setString(
                    2,
                    admin.getCorreo()
            );

            psUsuario.setString(
                    3,
                    admin.getPassword()
            );

            psUsuario.executeUpdate();

            rs = psUsuario.getGeneratedKeys();

            if (!rs.next()) {
                return false;
            }

            int usuarioId =
                    rs.getInt(1);

            // INSERT EN ADMINISTRADOR
            String sqlAdmin =
                    "INSERT INTO administrador(usuario_id) "
                    + "VALUES (?)";

            psAdmin =
                    conn.prepareStatement(sqlAdmin);

            psAdmin.setInt(
                    1,
                    usuarioId
            );

            return psAdmin.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error insertar admin: "
                    + e.getMessage()
            );

            return false;

        } finally {

            try {
                if (rs != null) rs.close();
                if (psUsuario != null) psUsuario.close();
                if (psAdmin != null) psAdmin.close();
            } catch (Exception e) {
            }
        }
    }

    public ArrayList<Administrador>
            listarAdministradores() {

        ArrayList<Administrador> lista =
                new ArrayList<>();

        try {

            Connection conn =
                    ConexionDB.conectar();

            String sql =
                    "SELECT a.id, u.nombre, "
                    + "u.correo, u.password_hash "
                    + "FROM administrador a "
                    + "INNER JOIN usuario u "
                    + "ON a.usuario_id = u.id";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                Administrador a =
                        new Administrador();

                a.setId(
                        rs.getInt("id")
                );

                a.setNombre(
                        rs.getString("nombre")
                );

                a.setCorreo(
                        rs.getString("correo")
                );

                a.setPassword(
                        rs.getString("password_hash")
                );

                lista.add(a);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error listar admins: "
                    + e.getMessage()
            );
        }

        return lista;
    }
}