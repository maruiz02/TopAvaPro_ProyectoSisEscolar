package itz.util;

/**
 * Clase utilitaria para validar que una contraseña cumpla los
 * requisitos de seguridad del sistema SisGesco.
 *
 * Reglas:
 *  - Entre 8 y 24 caracteres
 *  - Al menos una letra mayúscula
 *  - Al menos una letra minúscula
 *  - Al menos un dígito numérico
 *  - Al menos un carácter especial: !@#$%^&*()_+-=[]{}|;':",.<>?/`~\
 */
public class ValidadorPassword {

    // Caracteres especiales permitidos
    private static final String ESPECIALES = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\\";

    // Constructor privado: clase utilitaria
    private ValidadorPassword() {}

    /**
     * Verifica que la contraseña cumpla todas las reglas.
     *
     * @param password  contraseña a evaluar
     * @return          null si es válida, o un String con el mensaje de error
     */
    public static String validar(String password) {
        if (password == null || password.isEmpty()) {
            return "La contraseña no puede estar vacía.";
        }//Fin if

        // Regla 1: longitud
        if (password.length() < 8 || password.length() > 24) {
            return "La contraseña debe tener entre 8 y 24 caracteres.";
        }//Fin if

        boolean tieneMayuscula  = false;
        boolean tieneMinuscula  = false;
        boolean tieneNumero     = false;
        boolean tieneEspecial   = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))          tieneMayuscula = true;
            else if (Character.isLowerCase(c))     tieneMinuscula = true;
            else if (Character.isDigit(c))         tieneNumero    = true;
            else if (ESPECIALES.indexOf(c) >= 0)   tieneEspecial  = true;
        }//Fin for

        // Regla 2: mayúscula
        if (!tieneMayuscula) {
            return "La contraseña debe contener al menos una letra mayúscula.";
        }//Fin if

        // Regla 3: minúscula
        if (!tieneMinuscula) {
            return "La contraseña debe contener al menos una letra minúscula.";
        }//Fin if

        // Regla 4: número
        if (!tieneNumero) {
            return "La contraseña debe contener al menos un número.";
        }//Fin if

        // Regla 5: carácter especial
        if (!tieneEspecial) {
            return "La contraseña debe contener al menos un carácter especial "
                 + "( !@#$%^&*()_+- etc. ).";
        }//Fin if

        return null; // contraseña válida
    }//Fin validar

    /**
     * Devuelve un mensaje con todos los requisitos de la contraseña,
     * útil para mostrarlo como tooltip o instrucción al usuario.
     */
    public static String obtenerRequisitos() {
        return "Requisitos de contraseña:\n"
             + "  • Entre 8 y 24 caracteres\n"
             + "  • Al menos una letra mayúscula (A-Z)\n"
             + "  • Al menos una letra minúscula (a-z)\n"
             + "  • Al menos un número (0-9)\n"
             + "  • Al menos un carácter especial (!@#$%&*...)";
    }//Fin obtenerRequisitos

}//Fin de la clase