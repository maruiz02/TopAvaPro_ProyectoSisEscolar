package itz.reporte;

import itz.modelo.Alumno;

import java.io.File;

/**
 * Tarea Runnable que genera el boletin HTML de un alumno en un hilo separado.
 * Reporta progreso en 4 pasos a través de ProgresoCallback.
 */
public class TareaBoletinCalificaciones implements Runnable {

    private final Alumno           alumno;
    private final ProgresoCallback callback;

    /**
     * @param alumno    Alumno cuyo boletin se va a generar
     * @param callback  Receptor de actualizaciones de progreso (0–100)
     */
    public TareaBoletinCalificaciones(Alumno alumno, ProgresoCallback callback) {
        this.alumno   = alumno;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            // Paso 1 — Preparar directorio
            callback.onProgreso(10, "Preparando directorio de reportes...");
            new File("reportes").mkdirs();

            // Paso 2 — Consultar BD (dentro de GeneradorPDF)
            callback.onProgreso(35, "Consultando calificaciones de " + alumno.getNombre() + "...");

            String ruta = GeneradorPDF.rutaBoletin(alumno.getMatricula());

            // Paso 3 — Escribir archivo
            callback.onProgreso(70, "Generando documento HTML...");
            GeneradorPDF.generarBoletin(alumno, ruta);

            // Paso 4 — Listo
            callback.onProgreso(100, "Boletín generado: " + ruta);

        } catch (Exception e) {
            callback.onProgreso(-1, "Error al generar boletín: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
