package itz.reporte;

import itz.dao.CalificacionDAO;
import itz.modelo.Alumno;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Genera reportes en formato HTML para cada alumno.
 * Consulta la BD a través de CalificacionDAO (vista v_kardex).
 */
public class GeneradorPDF {

    // Formato de fecha en español: "29 de abril de 2026"
    private static final DateTimeFormatter FMT_FECHA =
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "MX"));

    // Sufijo del archivo: "20260429"
    private static final DateTimeFormatter FMT_ARCHIVO =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    // Calificacion minima para aprobar (escala 0-100)
    private static final double MINIMO_APROBATORIO = 60.0;

    // ─── CSS compartido ───────────────────────────────────────────────────────
    private static String css() {
        return """
            <style>
              * { box-sizing: border-box; margin: 0; padding: 0; }
              body { font-family: 'Segoe UI', Arial, sans-serif; background: #f4f6f8; }
              .contenedor { max-width: 850px; margin: 30px auto; background: white;
                            border-radius: 8px; overflow: hidden;
                            box-shadow: 0 4px 20px rgba(0,0,0,.15); }
              .encabezado { background: linear-gradient(135deg, #2980b9, #1a5276);
                            color: white; padding: 25px 30px; }
              .encabezado h1 { font-size: 22px; margin-bottom: 4px; }
              .encabezado h2 { font-size: 14px; font-weight: 400; opacity: .85; }
              .cuerpo { padding: 30px; }
              .datos { display: grid; grid-template-columns: 1fr 1fr;
                       gap: 8px 20px; margin-bottom: 25px; padding: 15px;
                       background: #eaf4fc; border-left: 4px solid #2980b9;
                       border-radius: 4px; }
              .datos p { font-size: 13px; color: #34495e; }
              .datos span { font-weight: 600; }
              table { width: 100%; border-collapse: collapse; font-size: 13px; }
              th { background: #34495e; color: white; padding: 10px 8px; text-align: left; }
              tr.par   td { background: #ecf0f1; }
              tr.impar td { background: #ffffff; }
              td { padding: 9px 8px; border-bottom: 1px solid #dde; }
              .aprobado  { color: #27ae60; font-weight: 700; }
              .reprobado { color: #c0392b; font-weight: 700; }
              .promedio { margin-top: 20px; padding: 14px 20px;
                          background: #fafafa; border-radius: 6px; }
              .promedio-ok  { border: 2px solid #27ae60; }
              .promedio-ok  h3 { font-size: 18px; color: #27ae60; }
              .promedio-mal { border: 2px solid #c0392b; }
              .promedio-mal h3 { font-size: 18px; color: #c0392b; }
              .pie { padding: 15px 30px; background: #ecf0f1; font-size: 11px;
                     color: #7f8c8d; display: flex; justify-content: space-between; }
              @media print {
                body { background: white; }
                .contenedor { box-shadow: none; margin: 0; }
              }
            </style>
            """;
    }

    // ─── Boletin de calificaciones ────────────────────────────────────────────

    /**
     * Genera el boletin HTML del alumno con sus calificaciones actuales desde la BD.
     *
     * @param alumno  Objeto con nombre, matricula y correo
     * @param ruta    Ruta completa del archivo a escribir
     */
    public static void generarBoletin(Alumno alumno, String ruta) throws IOException {

        CalificacionDAO dao = new CalificacionDAO();
        // kardex: [nombre_materia, valor, fecha_registro, promedio_general]
        List<Object[]> kardex = dao.obtenerKardex(alumno.getMatricula());

        String fechaHoy  = LocalDate.now().format(FMT_FECHA);
        double promedio  = kardex.isEmpty() ? 0.0
                         : (double) kardex.get(kardex.size() - 1)[3];

        StringBuilder filas = new StringBuilder();
        int contador = 1;
        for (Object[] fila : kardex) {
            String nombreMateria = (String)  fila[0];
            double valor         = (double)  fila[1];
            // clave_materia no viene de obtenerKardex; consultamos clave por nombre
            // Para no romper la firma del DAO existente, usamos el índice del kardex
            boolean aprobado = valor >= MINIMO_APROBATORIO;
            String clase     = aprobado ? "aprobado" : "reprobado";
            String estado    = aprobado ? "✔ APROBADO" : "✘ REPROBADO";
            String paridad   = (contador % 2 == 0) ? "par" : "impar";

            filas.append(String.format(
                "<tr class='%s'><td>%d</td><td>%s</td>"
                + "<td class='%s'>%.1f</td><td class='%s'>%s</td></tr>\n",
                paridad, contador, esc(nombreMateria),
                clase, valor, clase, estado));
            contador++;
        }

        if (filas.isEmpty()) {
            filas.append("<tr><td colspan='4' style='text-align:center;color:#999'>"
                       + "Sin calificaciones registradas</td></tr>");
        }

        boolean promedioOk = promedio >= MINIMO_APROBATORIO;
        String clsPromedio = promedioOk ? "promedio-ok" : "promedio-mal";
        String txtPromedio = promedioOk ? "APROBADO" : "REPROBADO";

        String html = "<!DOCTYPE html>\n<html lang=\"es\">\n<head>\n"
            + "<meta charset=\"UTF-8\">\n"
            + "<title>Boletín de Calificaciones — " + esc(alumno.getNombre()) + "</title>\n"
            + css()
            + "</head>\n<body>\n"
            + "<div class=\"contenedor\">\n"
            + "  <div class=\"encabezado\">\n"
            + "    <h1>&#127891; Instituto Tecnológico de Zacatecas</h1>\n"
            + "    <h2>Boletín Oficial de Calificaciones</h2>\n"
            + "  </div>\n"
            + "  <div class=\"cuerpo\">\n"
            + "    <div class=\"datos\">\n"
            + "      <p>Alumno: <span>" + esc(alumno.getNombre()) + "</span></p>\n"
            + "      <p>Matrícula: <span>" + esc(alumno.getMatricula()) + "</span></p>\n"
            + "      <p>Correo: <span>" + esc(alumno.getCorreo()) + "</span></p>\n"
            + "      <p>Fecha de emisión: <span>" + fechaHoy + "</span></p>\n"
            + "    </div>\n"
            + "    <h3 style=\"margin-bottom:12px;color:#2c3e50;\">&#128203; Calificaciones por Materia</h3>\n"
            + "    <table>\n"
            + "      <thead><tr><th>#</th><th>Materia</th>"
            + "<th>Calificación</th><th>Estado</th></tr></thead>\n"
            + "      <tbody>" + filas + "</tbody>\n"
            + "    </table>\n"
            + "    <div class=\"promedio " + clsPromedio + "\">\n"
            + "      <h3>Promedio General: " + String.format("%.2f", promedio)
            + " — " + txtPromedio + "</h3>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "  <div class=\"pie\">\n"
            + "    <span>Documento generado automáticamente por SisGesco · " + fechaHoy + "</span>\n"
            + "    <span>Firma: ________________________</span>\n"
            + "  </div>\n"
            + "</div>\n</body>\n</html>\n";

        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write(html);
        }
    }

    // ─── Historial académico ──────────────────────────────────────────────────

    /**
     * Genera el historial académico HTML: todas las materias inscritas,
     * con o sin calificación (muestra "Pendiente" si aún no tiene nota).
     */
    public static void generarHistorial(Alumno alumno, String ruta) throws IOException {

        CalificacionDAO calDAO = new CalificacionDAO();
        itz.dao.MateriaDAO matDAO = new itz.dao.MateriaDAO();

        // Materias inscritas: [clave, nombre, horario]
        List<Object[]> inscritas = matDAO.obtenerMateriasInscritasParaTabla(alumno.getMatricula());
        // Kardex: [nombre_materia, valor, fecha, promedio]
        List<Object[]> kardex   = calDAO.obtenerKardex(alumno.getMatricula());

        // Crear mapa nombre -> calificacion para cruzar
        java.util.Map<String, Double> mapaCal = new java.util.LinkedHashMap<>();
        double promedio = 0.0;
        for (Object[] k : kardex) {
            mapaCal.put((String) k[0], (double) k[1]);
            promedio = (double) k[3];
        }

        String fechaHoy = LocalDate.now().format(FMT_FECHA);

        StringBuilder filas = new StringBuilder();
        int contador = 1;
        int aprobadas = 0, reprobadas = 0, pendientes = 0;

        for (Object[] mat : inscritas) {
            String nombre  = (String) mat[1];
            String clave   = (String) mat[0];
            String horario = (String) mat[2];
            String paridad = (contador % 2 == 0) ? "par" : "impar";

            if (mapaCal.containsKey(nombre)) {
                double val    = mapaCal.get(nombre);
                boolean ok    = val >= MINIMO_APROBATORIO;
                String clase  = ok ? "aprobado" : "reprobado";
                String estado = ok ? "✔ Aprobado" : "✘ Reprobado";
                if (ok) aprobadas++; else reprobadas++;
                filas.append(String.format(
                    "<tr class='%s'><td>%d</td><td>%s</td><td>%s</td><td>%s</td>"
                    + "<td class='%s'>%.1f</td><td class='%s'>%s</td></tr>\n",
                    paridad, contador, esc(clave), esc(nombre), esc(horario),
                    clase, val, clase, estado));
            } else {
                pendientes++;
                filas.append(String.format(
                    "<tr class='%s'><td>%d</td><td>%s</td><td>%s</td><td>%s</td>"
                    + "<td style='color:#999'>—</td><td style='color:#e67e22'>⏳ Pendiente</td></tr>\n",
                    paridad, contador, esc(clave), esc(nombre), esc(horario)));
            }
            contador++;
        }

        if (filas.isEmpty()) {
            filas.append("<tr><td colspan='6' style='text-align:center;color:#999'>"
                       + "Sin materias inscritas</td></tr>");
        }

        boolean promedioOk = promedio >= MINIMO_APROBATORIO;
        String clsPromedio = promedioOk ? "promedio-ok" : "promedio-mal";

        String html = "<!DOCTYPE html>\n<html lang=\"es\">\n<head>\n"
            + "<meta charset=\"UTF-8\">\n"
            + "<title>Historial Académico — " + esc(alumno.getNombre()) + "</title>\n"
            + css()
            + "</head>\n<body>\n"
            + "<div class=\"contenedor\">\n"
            + "  <div class=\"encabezado\" style=\"background:linear-gradient(135deg,#1e8449,#145a32)\">\n"
            + "    <h1>&#127891; Instituto Tecnológico de Zacatecas</h1>\n"
            + "    <h2>Historial Académico Completo</h2>\n"
            + "  </div>\n"
            + "  <div class=\"cuerpo\">\n"
            + "    <div class=\"datos\">\n"
            + "      <p>Alumno: <span>" + esc(alumno.getNombre()) + "</span></p>\n"
            + "      <p>Matrícula: <span>" + esc(alumno.getMatricula()) + "</span></p>\n"
            + "      <p>Correo: <span>" + esc(alumno.getCorreo()) + "</span></p>\n"
            + "      <p>Fecha de emisión: <span>" + fechaHoy + "</span></p>\n"
            + "    </div>\n"
            + "    <div class=\"datos\" style=\"background:#eafaf1;border-color:#1e8449;margin-bottom:20px\">\n"
            + "      <p>Materias inscritas: <span>" + inscritas.size() + "</span></p>\n"
            + "      <p>Aprobadas: <span class='aprobado'>" + aprobadas + "</span></p>\n"
            + "      <p>Reprobadas: <span class='reprobado'>" + reprobadas + "</span></p>\n"
            + "      <p>Pendientes: <span style='color:#e67e22'>" + pendientes + "</span></p>\n"
            + "    </div>\n"
            + "    <h3 style=\"margin-bottom:12px;color:#2c3e50;\">&#128218; Historial de Materias</h3>\n"
            + "    <table>\n"
            + "      <thead><tr><th>#</th><th>Clave</th><th>Materia</th>"
            + "<th>Horario</th><th>Calificación</th><th>Estado</th></tr></thead>\n"
            + "      <tbody>" + filas + "</tbody>\n"
            + "    </table>\n"
            + (kardex.isEmpty() ? "" :
                "    <div class=\"promedio " + clsPromedio + "\">\n"
              + "      <h3>Promedio General (materias calificadas): "
              + String.format("%.2f", promedio) + "</h3>\n"
              + "    </div>\n")
            + "  </div>\n"
            + "  <div class=\"pie\">\n"
            + "    <span>Documento generado automáticamente por SisGesco · " + fechaHoy + "</span>\n"
            + "    <span>Firma: ________________________</span>\n"
            + "  </div>\n"
            + "</div>\n</body>\n</html>\n";

        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write(html);
        }
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    /** Retorna la ruta del boletin con fecha: reportes/boletin_A001_20260429.html */
    public static String rutaBoletin(String matricula) {
        return "reportes/boletin_" + matricula
             + "_" + LocalDate.now().format(FMT_ARCHIVO) + ".html";
    }

    /** Retorna la ruta del historial con fecha: reportes/historial_A001_20260429.html */
    public static String rutaHistorial(String matricula) {
        return "reportes/historial_" + matricula
             + "_" + LocalDate.now().format(FMT_ARCHIVO) + ".html";
    }

    /** Escapa caracteres HTML básicos para evitar inyección en el reporte. */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
