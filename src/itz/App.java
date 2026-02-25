package itz;

import itz.vista.VentanaLogin;
import itz.controlador.ControladorLogin;

public class App {

    public static void main(String[] args) {

        VentanaLogin vista = new VentanaLogin();
        new ControladorLogin(vista);
    }
}