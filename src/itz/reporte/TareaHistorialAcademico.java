package itz.reporte;

import itz.modelo.Alumno;

import java.io.File;

/**
 * Tarea Runnable que genera el historial académico HTML de un alumno
 * en un hilo separado. Reporta progreso en 4 pasos.
 */
public class TareaHistorialAcademico implements Runnable {

    private final Alumno           alumno;
    private final ProgresoCallback callback;

    /**
     * @param alumno    Alumno cuyo historial se va a generar
     * @param callback  Receptor de actualizaciones de progreso (0–100)
     */
    public TareaHistorialAcademico(Alumno alumno, ProgresoCallback callback) {
        this.alumno   = alumno;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            // Paso 1
            callback.onProgreso(10, "Preparando directorio de reportes...");
            new File("reportes").mkdirs();

            // Paso 2
            callback.onProgreso(35, "Consultando historial de " + alumno.getNombre() + "...");

            String ruta = GeneradorPDF.rutaHistorial(alumno.getMatricula());

            // Paso 3
            callback.onProgreso(70, "Generando documento HTML...");
            GeneradorPDF.generarHistorial(alumno, ruta);

            // Paso 4
            callback.onProgreso(100, "Historial generado: " + ruta);

        } catch (Exception e) {
            callback.onProgreso(-1, "Error al generar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
