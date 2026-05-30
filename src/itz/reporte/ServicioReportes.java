package itz.reporte;

import itz.modelo.Alumno;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servicio central de reportes.
 *
 * Responsabilidades:
 *  - Mantiene un ExecutorService con pool de 2 hilos (uno por tipo de reporte).
 *  - Lanza las tareas Runnable en segundo plano.
 *  - Actualiza la UI (JProgressBar + JLabel) desde el EDT usando SwingUtilities.invokeLater.
 *  - Ofrece apertura automática del reporte en el navegador al terminar.
 */
public class ServicioReportes {

    // Pool fijo: máximo 2 reportes corriendo al mismo tiempo (boletin e historial)
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // ─── Boletín ─────────────────────────────────────────────────────────────

    /**
     * Genera el boletín del alumno en un hilo separado.
     *
     * @param alumno      Alumno objetivo
     * @param progressBar Barra a actualizar (puede ser null si no se usa)
     * @param lblEstado   Label con mensaje de estado (puede ser null)
     * @param btnOrigen   Botón que disparó la acción; se deshabilita mientras corre
     */
    public void generarBoletin(Alumno alumno,
                                JProgressBar progressBar,
                                JLabel lblEstado,
                                JButton btnOrigen) {

        if (btnOrigen != null) btnOrigen.setEnabled(false);
        resetUI(progressBar, lblEstado, "Iniciando generación de boletín...");

        ProgresoCallback cb = construirCallback(progressBar, lblEstado, btnOrigen, true);
        executor.submit(new TareaBoletinCalificaciones(alumno, cb));
    }

    // ─── Historial ────────────────────────────────────────────────────────────

    /**
     * Genera el historial académico del alumno en un hilo separado.
     */
    public void generarHistorial(Alumno alumno,
                                  JProgressBar progressBar,
                                  JLabel lblEstado,
                                  JButton btnOrigen) {

        if (btnOrigen != null) btnOrigen.setEnabled(false);
        resetUI(progressBar, lblEstado, "Iniciando generación de historial...");

        ProgresoCallback cb = construirCallback(progressBar, lblEstado, btnOrigen, false);
        executor.submit(new TareaHistorialAcademico(alumno, cb));
    }

    // ─── Helpers privados ─────────────────────────────────────────────────────

    /**
     * Crea el callback que actualiza la UI en el EDT y, al llegar al 100%,
     * vuelve a habilitar el botón y abre el archivo en el navegador.
     *
     * @param esBoletin  true = boletín, false = historial (para abrir el archivo correcto)
     */
    private ProgresoCallback construirCallback(JProgressBar progressBar,
                                               JLabel lblEstado,
                                               JButton btnOrigen,
                                               boolean esBoletin) {
        return (porcentaje, mensaje) -> {
            // SwingUtilities.invokeLater garantiza que los cambios de UI
            // ocurran en el Event Dispatch Thread, no en el hilo worker.
            SwingUtilities.invokeLater(() -> {

                if (porcentaje == -1) {
                    // Error
                    if (lblEstado   != null) lblEstado.setText("⚠ " + mensaje);
                    if (progressBar != null) progressBar.setValue(0);
                    if (btnOrigen   != null) btnOrigen.setEnabled(true);
                    JOptionPane.showMessageDialog(null, mensaje, "Error al generar reporte",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (progressBar != null) progressBar.setValue(porcentaje);
                if (lblEstado   != null) lblEstado.setText(mensaje);

                if (porcentaje == 100) {
                    if (btnOrigen != null) btnOrigen.setEnabled(true);
                    // Extraer la ruta del mensaje ("...: reportes/xxx.html")
                    String ruta = mensaje.contains(": ") ? mensaje.split(": ", 2)[1] : "";
                    abrirReporte(ruta);
                }
            });
        };
    }

    /** Resetea la barra y el label al inicio de una nueva tarea. */
    private void resetUI(JProgressBar progressBar, JLabel lblEstado, String msgInicial) {
        SwingUtilities.invokeLater(() -> {
            if (progressBar != null) {
                progressBar.setValue(0);
                progressBar.setStringPainted(true);
            }
            if (lblEstado != null) lblEstado.setText(msgInicial);
        });
    }

    /**
     * Abre el archivo HTML en el navegador predeterminado del sistema.
     * Si falla (o el SO no soporta Desktop), muestra un mensaje con la ruta.
     */
    private void abrirReporte(String ruta) {
        if (ruta == null || ruta.isBlank()) return;
        File archivo = new File(ruta);
        if (!archivo.exists()) return;

        try {
            if (Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(archivo.toURI());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Reporte guardado en:\n" + archivo.getAbsolutePath(),
                        "Reporte generado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Reporte guardado en:\n" + archivo.getAbsolutePath(),
                    "Reporte generado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** Cierra el pool de hilos. Llamar al cerrar la aplicación. */
    public void shutdown() {
        executor.shutdown();
    }
}
