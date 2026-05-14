package itz.reporte;

import itz.modelo.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

public class TareaBoletinCalificaciones implements Callable<File> {

    private final Alumno alumno;
    private final String carpetaSalida;
    private final ProgresoCallback callback;

    public TareaBoletinCalificaciones(Alumno alumno,
                                       String carpetaSalida,
                                       ProgresoCallback callback) {
        this.alumno       = alumno;
        this.carpetaSalida = carpetaSalida;
        this.callback     = callback;
    }

    //  call() — corre en el hilo del pool, NO en el EDT de Swing
    @Override
    public File call() throws Exception {
        // Nombre de archivo seguro: sin caracteres especiales
        String nombreArchivo = String.format("boletin_%s_%s.html",
                alumno.getMatricula().replaceAll("[^a-zA-Z0-9]", "_"),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        File archivo = new File(carpetaSalida, nombreArchivo);

        callback.onProgreso(10);

        // Construir el HTML con diseño
        String contenido = construirHTML();
        callback.onProgreso(70);

        // Escribir el archivo (I/O en hilo de fondo, sin bloquear UI)
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivo, java.nio.charset.StandardCharsets.UTF_8))) {
            bw.write(contenido);
        }
        callback.onProgreso(90);

        // Simular tiempo de procesamiento
        Thread.sleep(300);

        callback.onProgreso(100);
        return archivo;
    }

    //  Construcción del HTML del boletín
    private String construirHTML() {
        List<Calificacion> cals = alumno.getKardex().getHistorial();
        double promedio = alumno.getKardex().calcularPromedio();
        String fecha = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                        new java.util.Locale("es", "MX")));

        StringBuilder filas = new StringBuilder();
        for (int i = 0; i < cals.size(); i++) {
            Calificacion c = cals.get(i);
            double val = c.getValor();
            boolean aprobado = val >= 6.0;
            String clase  = aprobado ? "aprobado" : "reprobado";
            String estado = aprobado ? "✔ APROBADO" : "✘ REPROBADO";
            filas.append(String.format(
                    "<tr class='%s-fila'>" +
                    "<td>%d</td>" +
                    "<td>%s</td>" +
                    "<td>%s</td>" +
                    "<td class='%s'>%.1f</td>" +
                    "<td class='%s'>%s</td>" +
                    "</tr>",
                    (i % 2 == 0 ? "par" : "impar"),
                    i + 1,
                    escapeHtml(c.getMateria().getNombre()),
                    escapeHtml(c.getMateria().getClave()),
                    clase, val,
                    clase, estado
            ));
        }//Fin for

        if (cals.isEmpty()) {
            filas.append("<tr><td colspan='5' style='text-align:center;color:#7f8c8d;'>" +
                         "Sin calificaciones registradas</td></tr>");
        }//Fin if 

        String colorPromedio = promedio >= 6.0 ? "#27ae60" : "#c0392b";

        return String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Boletín de Calificaciones — %s</title>
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
  table { width: 100%%; border-collapse: collapse; font-size: 13px; }
  th { background: #34495e; color: white; padding: 10px 8px; text-align: left; }
  tr.par td   { background: #ecf0f1; }
  tr.impar td { background: #ffffff; }
  td { padding: 9px 8px; border-bottom: 1px solid #dde; }
  .aprobado  { color: #27ae60; font-weight: 700; }
  .reprobado { color: #c0392b; font-weight: 700; }
  .promedio { margin-top: 20px; padding: 14px 20px;
              background: #fafafa; border-radius: 6px;
              border: 2px solid %s; }
  .promedio h3 { font-size: 18px; color: %s; }
  .pie { padding: 15px 30px; background: #ecf0f1; font-size: 11px;
         color: #7f8c8d; display: flex; justify-content: space-between; }
  @media print {
    body { background: white; }
    .contenedor { box-shadow: none; margin: 0; }
  }
</style>
</head>
<body>
<div class="contenedor">
  <div class="encabezado">
    <h1>🎓 Instituto Tecnológico de Zacatecas</h1>
    <h2>Boletín Oficial de Calificaciones</h2>
  </div>
  <div class="cuerpo">
    <div class="datos">
      <p>Alumno: <span>%s</span></p>
      <p>Matrícula: <span>%s</span></p>
      <p>Correo: <span>%s</span></p>
      <p>Fecha de emisión: <span>%s</span></p>
    </div>
    <h3 style="margin-bottom:12px;color:#2c3e50;">📋 Calificaciones por Materia</h3>
    <table>
      <thead>
        <tr>
          <th>#</th><th>Materia</th><th>Clave</th>
          <th>Calificación</th><th>Estado</th>
        </tr>
      </thead>
      <tbody>%s</tbody>
    </table>
    <div class="promedio">
      <h3>Promedio General: %.2f — %s</h3>
    </div>
  </div>
  <div class="pie">
    <span>Documento generado automáticamente por SisGesco · %s</span>
    <span>Firma: ________________________</span>
  </div>
</div>
</body>
</html>
""",
                // <title>
                escapeHtml(alumno.getNombre()),
                // .promedio border-color, color
                colorPromedio, colorPromedio,
                // datos alumno
                escapeHtml(alumno.getNombre()),
                escapeHtml(alumno.getMatricula()),
                escapeHtml(alumno.getCorreo()),
                fecha,
                // tabla
                filas.toString(),
                // promedio
                promedio, promedio >= 6.0 ? "APROBADO" : "REPROBADO",
                // pie fecha
                fecha
        );
    }

    private static String escapeHtml(String s) {
        if (s == null){ 
            return "";
        }//Fin if 
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}//Fin de la clase
