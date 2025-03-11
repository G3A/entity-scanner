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

@Component
public class CsvReportExporter {

    public void export(List<EntityInfo> validEntities, List<EntityInfo> invalidEntities, String sourcePath) {
        try {
            // Crear nombre de archivo con timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String fileName = "entidades_" + timestamp + ".csv";

            // Crear directorio de salida si no existe
            Path outputDir = Paths.get("output");
            if (!Files.exists(outputDir)) {
                Files.createDirectory(outputDir);
            }

            File csvFile = new File(outputDir.toString(), fileName);

            try (FileWriter writer = new FileWriter(csvFile, StandardCharsets.UTF_8)) {
                // Encabezados
                writer.write("Nombre Tabla,Esquema,Nombre Clase,Paquete,Ruta del Archivo\n");

                // Datos de entidades válidas
                for (EntityInfo info : validEntities) {
                    writer.write(String.format("%s,%s,%s,%s,%s\n",
                            escapeCsv(info.getTableName()),
                            escapeCsv(info.getSchema()),
                            escapeCsv(info.getClassName()),
                            escapeCsv(info.getPackageName()),
                            escapeCsv(info.getFilePath())));
                }
            }

            System.out.println("Los datos han sido exportados a CSV: " + csvFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al exportar a CSV: " + e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        // Escapar comillas y caracteres especiales
        value = value.replace("\"", "\"\"");
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = "\"" + value + "\"";
        }
        return value;
    }
}