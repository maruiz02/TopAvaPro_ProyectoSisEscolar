package itz.util;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NavegacionTeclado {

    public static void registrar(JTextField... campos) {
        for (int i = 0; i < campos.length; i++) {
            final int indice = i;
            campos[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int codigo = e.getKeyCode();

                    // Flecha abajo → siguiente campo
                    if (codigo == KeyEvent.VK_DOWN && indice < campos.length - 1) {
                        campos[indice + 1].requestFocusInWindow();
                        e.consume(); // Evita que la flecha mueva el cursor dentro del texto
                    } // Flecha arriba → campo anterior
                    else if (codigo == KeyEvent.VK_UP && indice > 0) {
                        campos[indice - 1].requestFocusInWindow();
                        e.consume();
                    }//Fin if-else
                }
            });
        }//Fin for
    }
}//Fin de la clase
