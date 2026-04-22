package itz.reporte;

import itz.modelo.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

public class TareaHistorialAcademico implements Callable<File> {

    private final Alumno alumno;
    private final String carpetaSalida;
    private final ProgresoCallback callback;

    public TareaHistorialAcademico(Alumno alumno,
            String carpetaSalida,
            ProgresoCallback callback) {
        this.alumno = alumno;
        this.carpetaSalida = carpetaSalida;
        this.callback = callback;
    }

    @Override
    public File call() throws Exception {
        String nombreArchivo = String.format("historial_%s_%s.html",
                alumno.getMatricula().replaceAll("[^a-zA-Z0-9]", "_"),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        File archivo = new File(carpetaSalida, nombreArchivo);

        callback.onProgreso(10);
        String contenido = construirHTML();
        callback.onProgreso(70);

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivo, java.nio.charset.StandardCharsets.UTF_8))) {
            bw.write(contenido);
        }
        callback.onProgreso(90);

        Thread.sleep(300);
        callback.onProgreso(100);
        return archivo;
    }

    private String construirHTML() {
        List<Calificacion> historial = alumno.getKardex().getHistorial();
        double promedio = alumno.getKardex().calcularPromedio();
        long aprobadas = historial.stream().filter(c -> c.getValor() >= 6.0).count();
        long reprobadas = historial.size() - aprobadas;
        int total = historial.size();

        String fecha = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                        new java.util.Locale("es", "MX")));

        // Filas de la tabla
        StringBuilder filas = new StringBuilder();
        for (int i = 0; i < historial.size(); i++) {
            Calificacion c = historial.get(i);
            double val = c.getValor();
            boolean ok = val >= 6.0;
            filas.append(String.format(
                    "<tr>"
                    + "<td>%d</td>"
                    + "<td>%s</td>"
                    + "<td>%s</td>"
                    + "<td class='%s'>%.1f</td>"
                    + "<td class='%s'>%s</td>"
                    + "</tr>",
                    i + 1,
                    escapeHtml(c.getMateria().getNombre()),
                    escapeHtml(c.getMateria().getClave()),
                    ok ? "aprobado" : "reprobado", val,
                    ok ? "aprobado" : "reprobado",
                    ok ? "✔ APROBADO" : "✘ REPROBADO"
            ));
        }//Fin for

        if (historial.isEmpty()) {
            filas.append("<tr><td colspan='5' class='vacio'>"
                    + "No hay materias en el historial</td></tr>");
        }//Fin if 

        // Barras de progreso visual para la gráfica de estadísticas
        int pctAprobadas = total > 0 ? (int) ((aprobadas * 100.0) / total) : 0;
        int pctReprobadas = total > 0 ? (int) ((reprobadas * 100.0) / total) : 0;

        String colorPromedio = promedio >= 6.0 ? "#27ae60" : "#c0392b";

        return String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Historial Académico — %s</title>
<style>
  * { box-sizing: border-box; margin: 0; padding: 0; }
  body { font-family: 'Segoe UI', Arial, sans-serif; background: #f4f6f8; }
  .page { max-width: 900px; margin: 30px auto; background: white;
          border-radius: 8px; overflow: hidden;
          box-shadow: 0 4px 20px rgba(0,0,0,.15); }
  /* Encabezado */
  .header { background: linear-gradient(135deg, #8e44ad, #6c3483);
            color: white; padding: 25px 30px; }
  .header h1 { font-size: 22px; }
  .header h2 { font-size: 14px; font-weight: 400; opacity: .85; margin-top: 4px; }
  /* Datos alumno */
  .datos { display: grid; grid-template-columns: 1fr 1fr;
           gap: 8px 30px; padding: 20px 30px;
           background: #f5eef8; border-bottom: 2px solid #d2b4de; }
  .datos p { font-size: 13px; color: #4a235a; }
  .datos span { font-weight: 600; }
  /* Estadísticas */
  .stats { display: flex; gap: 20px; padding: 20px 30px;
           border-bottom: 1px solid #eee; }
  .stat-box { flex: 1; padding: 14px; border-radius: 8px; text-align: center; }
  .stat-box.total   { background: #eaf0fb; border: 2px solid #2980b9; }
  .stat-box.aprob   { background: #eafaf1; border: 2px solid #27ae60; }
  .stat-box.reprob  { background: #fdedec; border: 2px solid #c0392b; }
  .stat-box.prom    { background: #fef9e7; border: 2px solid %s; }
  .stat-box .num    { font-size: 28px; font-weight: 700; }
  .stat-box .lbl    { font-size: 11px; margin-top: 4px; color: #555; }
  .stat-box.total .num  { color: #2980b9; }
  .stat-box.aprob .num  { color: #27ae60; }
  .stat-box.reprob .num { color: #c0392b; }
  .stat-box.prom .num   { color: %s; }
  /* Barra de progreso */
  .barras { padding: 15px 30px; }
  .barra-row { display: flex; align-items: center; gap: 10px;
               margin-bottom: 8px; font-size: 12px; }
  .barra-bg  { flex: 1; height: 12px; background: #ecf0f1; border-radius: 6px; overflow: hidden; }
  .barra-fill{ height: 100%%; border-radius: 6px; }
  .fill-verde { background: #27ae60; }
  .fill-rojo  { background: #c0392b; }
  /* Tabla */
  .tabla-wrap { padding: 0 30px 20px; }
  table { width: 100%%; border-collapse: collapse; font-size: 13px; }
  th { background: #4a235a; color: white; padding: 10px 8px; text-align: left; }
  tr:nth-child(even) td { background: #f8f0fb; }
  td { padding: 9px 8px; border-bottom: 1px solid #e8d5f0; }
  .aprobado  { color: #27ae60; font-weight: 700; }
  .reprobado { color: #c0392b; font-weight: 700; }
  .vacio { text-align: center; color: #7f8c8d; padding: 20px; }
  /* Pie */
  .pie { padding: 14px 30px; background: #f4f6f7; font-size: 11px;
         color: #7f8c8d; display: flex; justify-content: space-between;
         border-top: 1px solid #d5d8dc; }
  @media print {
    body { background: white; }
    .page { box-shadow: none; margin: 0; }
  }
</style>
</head>
<body>
<div class="page">
  <!-- Encabezado -->
  <div class="header">
    <h1>🎓 Instituto Tecnológico de Zacatecas</h1>
    <h2>Historial Académico Oficial (KARDEX)</h2>
  </div>

  <!-- Datos del alumno -->
  <div class="datos">
    <p>Alumno: <span>%s</span></p>
    <p>Matrícula: <span>%s</span></p>
    <p>Correo electrónico: <span>%s</span></p>
    <p>Fecha de emisión: <span>%s</span></p>
  </div>

  <!-- Estadísticas -->
  <div class="stats">
    <div class="stat-box total">
      <div class="num">%d</div>
      <div class="lbl">TOTAL MATERIAS</div>
    </div>
    <div class="stat-box aprob">
      <div class="num">%d</div>
      <div class="lbl">APROBADAS</div>
    </div>
    <div class="stat-box reprob">
      <div class="num">%d</div>
      <div class="lbl">REPROBADAS</div>
    </div>
    <div class="stat-box prom">
      <div class="num">%.2f</div>
      <div class="lbl">PROMEDIO ACUMULADO</div>
    </div>
  </div>

  <!-- Barras visuales -->
  <div class="barras">
    <div class="barra-row">
      <span style="width:90px;color:#27ae60;">Aprobadas</span>
      <div class="barra-bg">
        <div class="barra-fill fill-verde" style="width:%d%%"></div>
      </div>
      <span>%d%%</span>
    </div>
    <div class="barra-row">
      <span style="width:90px;color:#c0392b;">Reprobadas</span>
      <div class="barra-bg">
        <div class="barra-fill fill-rojo" style="width:%d%%"></div>
      </div>
      <span>%d%%</span>
    </div>
  </div>

  <!-- Tabla detallada -->
  <div class="tabla-wrap">
    <h3 style="margin-bottom:12px;color:#4a235a;">📚 Detalle de Calificaciones</h3>
    <table>
      <thead>
        <tr><th>#</th><th>Materia</th><th>Clave</th>
            <th>Calificación</th><th>Estado</th></tr>
      </thead>
      <tbody>%s</tbody>
    </table>
  </div>

  <!-- Pie -->
  <div class="pie">
    <span>SisGesco · Documento oficial generado el %s</span>
    <span>Sello y firma: ________________________</span>
  </div>
</div>
</body>
</html>
""",
                // <title>
                escapeHtml(alumno.getNombre()),
                // stat-box.prom border y color (2 veces)
                colorPromedio, colorPromedio,
                // datos alumno
                escapeHtml(alumno.getNombre()),
                escapeHtml(alumno.getMatricula()),
                escapeHtml(alumno.getCorreo()),
                fecha,
                // stats
                total, aprobadas, reprobadas, promedio,
                // barras
                pctAprobadas, pctAprobadas,
                pctReprobadas, pctReprobadas,
                // tabla
                filas.toString(),
                // pie
                fecha
        );
    }

    private static String escapeHtml(String s) {
        if (s == null) {
            return "";
        }//Fin if 
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}//Fin de la clase
