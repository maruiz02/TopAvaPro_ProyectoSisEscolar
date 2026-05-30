package itz.reporte;

/**
 * Callback funcional para reportar el progreso de una tarea en segundo plano.
 * Se invoca desde el hilo worker y debe actualizar la UI usando SwingUtilities.invokeLater.
 */
@FunctionalInterface
public interface ProgresoCallback {
    /**
     * @param porcentaje  0-100
     * @param mensaje     Descripción del paso actual (puede mostrarse en un JLabel)
     */
    void onProgreso(int porcentaje, String mensaje);
}
