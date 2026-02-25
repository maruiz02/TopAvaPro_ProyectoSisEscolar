package itz.controlador;

import itz.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;

public class ControladorAdmin {

    private final String ARCHIVO = "usuarios.dat";
    private ArrayList<Usuario> listaUsuarios;

    public ControladorAdmin() {
        listaUsuarios = cargarUsuarios();
    }

    // REGISTRAR USUARIO
    public String registrarUsuario(String usuario, String password, String rol) {

        if (usuario == null || usuario.trim().isEmpty())
            return "Usuario vacío";

        if (password == null || password.trim().isEmpty())
            return "Contraseña vacía";

        if (rol == null || rol.trim().isEmpty())
            return "Rol vacío";

        if (usuarioExiste(usuario))
            return "El usuario ya existe";

        Usuario nuevo = new Usuario(usuario, password, rol.toUpperCase());
        listaUsuarios.add(nuevo);
        guardarUsuarios();

        return "OK";
    }

    // VERIFICAR SI EXISTE
    private boolean usuarioExiste(String user) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(user)) {
                return true;
            }
        }
        return false;
    }
    
    // GUARDAR
    private void guardarUsuarios() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CARGAR
    private ArrayList<Usuario> cargarUsuarios() {
        File file = new File(ARCHIVO);
        if (file.exists()) {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(ARCHIVO))) {
                return (ArrayList<Usuario>) ois.readObject();
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}