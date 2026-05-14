package itz.reporte;

import itz.modelo.*;
import java.awt.Window;
import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class ServicioReportes {

    // Pool de hilos compartido en toda la aplicación 
    private static final int NUM_HILOS = 4;
    private static final ExecutorService threadPool
            = Executors.newFixedThreadPool(NUM_HILOS);

    // Carpeta de salida 
    private static final String CARPETA_REPORTES = "reportes";

    //Constructor privado: clase de utilerías
    private ServicioReportes() {
    }

    //  1. BOLENTÍN DE CALIFICACIONES — un alumno
    public static void generarBoletinAsync(Alumno alumno, JComponent parent) {
        JProgressBar barra = crearDialogoProgreso(parent,
                "Generando boletín de " + alumno.getNombre() + "...");

        // SwingWorker: doInBackground() corre en un hilo del pool,
        // done() corre de vuelta en el EDT de Swing.
        SwingWorker<File, Integer> worker = new SwingWorker<>() {

            @Override
            protected File doInBackground() throws Exception {
                // Aseguramos que exista la carpeta de salida
                crearCarpeta();

                // Creamos la tarea y la enviamos al pool de hilos
                TareaBoletinCalificaciones tarea
                        = new TareaBoletinCalificaciones(alumno, CARPETA_REPORTES,
                                porcentaje -> publish(porcentaje));

                Future<File> futuro = threadPool.submit(tarea);

                // Monitoreamos el progreso mientras el hilo trabaja
                while (!futuro.isDone()) {
                    Thread.sleep(50);          // chequeo cada 50 ms
                }
                return futuro.get();           // resultado: el File del PDF
            }

            @Override
            protected void process(List<Integer> porcentajes) {
                // Este método corre en el EDT: actualiza la UI de forma segura
                barra.setValue(porcentajes.get(porcentajes.size() - 1));
            }

            @Override
            protected void done() {
                // Cierra el diálogo de progreso y notifica
                Window ventana = SwingUtilities.getWindowAncestor(barra);
                if (ventana != null) {
                    ventana.dispose();
                }//Fin if 
                try {
                    File pdf = get();
                    JOptionPane.showMessageDialog(parent,
                            "✅ Boletín generado:\n" + pdf.getAbsolutePath(),
                            "Reporte listo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent,
                            "❌ Error al generar el boletín:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // Historial academico (un alumno)
    public static void generarHistorialAsync(Alumno alumno, JComponent parent) {
        JProgressBar barra = crearDialogoProgreso(parent,
                "Generando historial de " + alumno.getNombre() + "...");

        SwingWorker<File, Integer> worker = new SwingWorker<>() {

            @Override
            protected File doInBackground() throws Exception {
                crearCarpeta();

                TareaHistorialAcademico tarea
                        = new TareaHistorialAcademico(alumno, CARPETA_REPORTES,
                                porcentaje -> publish(porcentaje));

                Future<File> futuro = threadPool.submit(tarea);

                while (!futuro.isDone()) {
                    Thread.sleep(50);
                }
                return futuro.get();
            }

            @Override
            protected void process(List<Integer> porcentajes) {
                barra.setValue(porcentajes.get(porcentajes.size() - 1));
            }

            @Override
            protected void done() {
                Window ventana = SwingUtilities.getWindowAncestor(barra);
                if (ventana != null) {
                    ventana.dispose();
                }//Fin if 
                try {
                    File pdf = get();
                    JOptionPane.showMessageDialog(parent,
                            "✅ Historial generado:\n" + pdf.getAbsolutePath(),
                            "Reporte listo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent,
                            "❌ Error al generar el historial:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // Reporte en lote para alumnos (Admin)
    public static void generarReportesLoteAsync(SistemaEscolar sistema,
            JComponent parent) {
        List<Alumno> alumnos = sistema.getAlumnos();
        if (alumnos.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay alumnos registrados.");
            return;
        }//Fin if 

        JProgressBar barra = crearDialogoProgreso(parent,
                "Generando reportes de " + alumnos.size() + " alumnos...");

        SwingWorker<List<File>, Integer> worker = new SwingWorker<>() {

            @Override
            protected List<File> doInBackground() throws Exception {
                crearCarpeta();
                List<Future<File>> futuros = new ArrayList<>();

                // Enviar TODAS las tareas al pool de una vez.
                // El pool decide cuántas corren en paralelo (NUM_HILOS).
                for (Alumno alumno : alumnos) {
                    TareaBoletinCalificaciones tarea
                            = new TareaBoletinCalificaciones(alumno, CARPETA_REPORTES,
                                    p -> {
                                    });   // sin progreso individual en lote
                    futuros.add(threadPool.submit(tarea));
                }//Fin for 

                // Recolectar resultados conforme van terminando
                List<File> archivos = new ArrayList<>();
                int completados = 0;
                for (Future<File> f : futuros) {
                    archivos.add(f.get());          // espera si ese hilo aún trabaja
                    completados++;
                    publish((completados * 100) / futuros.size());
                }//Fin for
                return archivos;
            }

            @Override
            protected void process(List<Integer> porcentajes) {
                barra.setValue(porcentajes.get(porcentajes.size() - 1));
            }

            @Override
            protected void done() {
                Window ventana = SwingUtilities.getWindowAncestor(barra);
                if (ventana != null) {
                    ventana.dispose();
                }//Fin if 
                try {
                    List<File> archivos = get();
                    JOptionPane.showMessageDialog(parent,
                            "✅ " + archivos.size() + " reportes generados en:\n"
                            + new File(CARPETA_REPORTES).getAbsolutePath(),
                            "Reportes listos", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent,
                            "❌ Error en generación por lotes:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    //  Utilidades internas
    //Creando carpeta si no existe
    private static void crearCarpeta() {
        new File(CARPETA_REPORTES).mkdirs();
    }

    private static JProgressBar crearDialogoProgreso(JComponent parent, String mensaje) {
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(new JLabel(mensaje));
        panel.add(barra);

        // Mostramos el panel en un JDialog no modal para no bloquear el EDT
        JDialog dialogo = new JDialog(
                SwingUtilities.getWindowAncestor(parent), "Generando PDF...");
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setContentPane(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(parent);
        dialogo.setVisible(true);

        return barra;
    }

    //Apagando el pool de hilos
    public static void apagar() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }//Fin if
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }
}//Fin de la clase
