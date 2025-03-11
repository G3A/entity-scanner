package co.g3a.entityscanner;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HtmlReportExporter {

    public void export(List<EntityInfo> validEntities, List<EntityInfo> invalidEntities, String sourcePath) {
        try {
            // Crear nombre de archivo con timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String fileName = "entidades_" + timestamp + ".html";

            // Crear directorio de salida si no existe
            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectory(outputDir);
            }

            File htmlFile = new File(outputDir.toString(), fileName);

            try (FileWriter writer = new FileWriter(htmlFile, StandardCharsets.UTF_8)) {
                generateHtmlReport(writer, validEntities, invalidEntities, sourcePath);
            }

            System.out.println("Los datos han sido exportados a HTML (abre con Excel): " + htmlFile.getAbsolutePath());
            System.out.println("Puedes abrir este archivo HTML con Excel o cualquier navegador para ver el reporte.");

        } catch (IOException e) {
            System.err.println("Error al exportar a HTML: " + e.getMessage());
        }
    }

    private void generateHtmlReport(FileWriter writer, List<EntityInfo> validEntities, 
                                   List<EntityInfo> invalidEntities, String sourcePath) throws IOException {
        writer.write("<!DOCTYPE html>\n");
        writer.write("<html>\n");
        writer.write("<head>\n");
        writer.write("    <meta charset=\"UTF-8\">\n");
        writer.write("    <title>Entidades JPA encontradas</title>\n");
        writer.write("    <style>\n");
        writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        writer.write("        table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }\n");
        writer.write("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        writer.write("        th { background-color: #f2f2f2; font-weight: bold; }\n");
        writer.write("        tr:nth-child(even) { background-color: #f9f9f9; }\n");
        writer.write("        tr:hover { background-color: #f1f1f1; }\n");
        writer.write("        h1, h2 { color: #333; }\n");
        writer.write("        .schema-header { background-color: #e6f2ff; font-weight: bold; }\n");
        writer.write("        .subtotal { background-color: #f2f2f2; font-weight: bold; }\n");
        writer.write("        .total { background-color: #d9edf7; font-weight: bold; font-size: 1.1em; }\n");
        writer.write("        .info { margin-bottom: 20px; }\n");
        writer.write("        .invalid { background-color: #f2dede; }\n");
        writer.write("    </style>\n");
        writer.write("</head>\n");
        writer.write("<body>\n");

        writeReportHeader(writer, validEntities, invalidEntities, sourcePath);
        writeEntitiesBySchema(writer, validEntities);
        writeSchemaSummary(writer, validEntities);
        writeInvalidEntities(writer, invalidEntities);

        writer.write("</body>\n");
        writer.write("</html>\n");
    }

    private void writeReportHeader(FileWriter writer, List<EntityInfo> validEntities, 
                                  List<EntityInfo> invalidEntities, String sourcePath) throws IOException {
        writer.write("    <h1>Entidades JPA encontradas</h1>\n");
        writer.write("    <div class=\"info\">\n");
        writer.write("        <p><strong>Fecha de generación:</strong> " + new Date() + "</p>\n");
        writer.write("        <p><strong>Ruta escaneada:</strong> " + escapeHtml(sourcePath) + "</p>\n");
        writer.write("        <p><strong>Total de entidades con @Table:</strong> " + validEntities.size() + "</p>\n");
        writer.write("        <p><strong>Total de entidades sin @Table:</strong> " + invalidEntities.size() + "</p>\n");
        writer.write("    </div>\n");
    }

    private void writeEntitiesBySchema(FileWriter writer, List<EntityInfo> validEntities) throws IOException {
        // Agrupar por esquema
        Map<String, List<EntityInfo>> entitiesBySchema = validEntities.stream()
                .collect(Collectors.groupingBy(EntityInfo::getSchema));

        for (Map.Entry<String, List<EntityInfo>> entry : entitiesBySchema.entrySet()) {
            String schema = entry.getKey();
            List<EntityInfo> schemaEntities = entry.getValue();

            // Título del esquema
            writer.write("    <h2>Esquema: " + escapeHtml(schema) + " (" + schemaEntities.size() + " tablas)</h2>\n");

            // Tabla para este esquema
            writer.write("    <table>\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Nombre Tabla</th>\n");
            writer.write("            <th>Esquema</th>\n");
            writer.write("            <th>Nombre Clase</th>\n");
            writer.write("            <th>Paquete</th>\n");
            writer.write("            <th>Ruta del Archivo</th>\n");
            writer.write("        </tr>\n");

            for (EntityInfo info : schemaEntities) {
                writer.write("        <tr>\n");
                writer.write("            <td>" + escapeHtml(info.getTableName()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getSchema()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getClassName()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getPackageName()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getFilePath()) + "</td>\n");
                writer.write("        </tr>\n");
            }

            // Subtotal para este esquema
            writer.write("        <tr class=\"subtotal\">\n");
            writer.write("            <td colspan=\"5\">Subtotal de tablas en esquema " +
                    escapeHtml(schema) + ": " + schemaEntities.size() + "</td>\n");
            writer.write("        </tr>\n");
            writer.write("    </table>\n");
        }

        // Resumen final
        writer.write("    <table class=\"total\">\n");
        writer.write("        <tr>\n");
        writer.write("            <td><strong>TOTAL DE ENTIDADES CON ANOTACIÓN @Table: " + validEntities.size() + "</strong></td>\n");
        writer.write("        </tr>\n");
        writer.write("    </table>\n");
    }

    private void writeSchemaSummary(FileWriter writer, List<EntityInfo> validEntities) throws IOException {
        // Agrupar por esquema
        Map<String, List<EntityInfo>> entitiesBySchema = validEntities.stream()
                .collect(Collectors.groupingBy(EntityInfo::getSchema));
                
        // Tabla de resumen por esquema
        writer.write("    <h2>Resumen por Esquema</h2>\n");
        writer.write("    <table>\n");
        writer.write("        <tr>\n");
        writer.write("            <th>Esquema</th>\n");
        writer.write("            <th>Número de Tablas</th>\n");
        writer.write("            <th>Porcentaje</th>\n");
        writer.write("        </tr>\n");

        for (Map.Entry<String, List<EntityInfo>> entry : entitiesBySchema.entrySet()) {
            String schema = entry.getKey();
            int count = entry.getValue().size();
            double percentage = (double) count / validEntities.size() * 100;

            writer.write("        <tr>\n");
            writer.write("            <td>" + escapeHtml(schema) + "</td>\n");
            writer.write("            <td>" + count + "</td>\n");
            writer.write("            <td>" + String.format("%.2f%%", percentage) + "</td>\n");
            writer.write("        </tr>\n");
        }

        writer.write("        <tr class=\"total\">\n");
        writer.write("            <td>TOTAL</td>\n");
        writer.write("            <td>" + validEntities.size() + "</td>\n");
        writer.write("            <td>100.00%</td>\n");
        writer.write("        </tr>\n");
        writer.write("    </table>\n");
    }

    private void writeInvalidEntities(FileWriter writer, List<EntityInfo> invalidEntities) throws IOException {
        if (!invalidEntities.isEmpty()) {
            writer.write("    <h2>Entidades sin anotación @Table (" + invalidEntities.size() + ")</h2>\n");
            writer.write("    <table class=\"invalid\">\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Nombre Clase</th>\n");
            writer.write("            <th>Paquete</th>\n");
            writer.write("            <th>Ruta del Archivo</th>\n");
            writer.write("        </tr>\n");

            for (EntityInfo info : invalidEntities) {
                writer.write("        <tr>\n");
                writer.write("            <td>" + escapeHtml(info.getClassName()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getPackageName()) + "</td>\n");
                writer.write("            <td>" + escapeHtml(info.getFilePath()) + "</td>\n");
                writer.write("        </tr>\n");
            }

            writer.write("    </table>\n");
        }
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}