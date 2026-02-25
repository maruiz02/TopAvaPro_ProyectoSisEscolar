package itz.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String usuario;      // será la matrícula si es alumno
    private String contrasena;
    private String rol;

    public Usuario(String usuario, String contrasena, String rol) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }
}