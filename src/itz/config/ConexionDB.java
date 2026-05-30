package itz.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3307/sisgesco?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Mexico_City";
    private static final String USER = "root";
    private static final String PASSWORD = "C34dZ23.0407";

    public static Connection conectar() {
        Connection conexion = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conexion exitosa");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }

        return conexion;
    }
}
