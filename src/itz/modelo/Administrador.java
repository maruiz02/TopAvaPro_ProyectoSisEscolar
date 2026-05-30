package itz.modelo;

public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(int id, String nombre,
                         String apellido, String correo,
                         String password) {

        super(id, nombre, apellido,
              correo, password,
              "ADMINISTRADOR");
    }
}