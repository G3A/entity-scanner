package co.g3a.entityscanner;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Lazy
@RequiredArgsConstructor
public class ReportGenerator {
    

    private final ConsoleReportExporter consoleExporter;
    

    private final CsvReportExporter csvExporter;
    

    private final HtmlReportExporter htmlExporter;

    public void generateReports(List<EntityInfo> allEntityInfos, String sourcePath) {
        // Filtrar entidades que tienen anotación @Table
        List<EntityInfo> validEntityInfos = allEntityInfos.stream()
                .filter(EntityInfo::hasTableAnnotation)
                .collect(Collectors.toList());

        // Identificar entidades sin anotación @Table
        List<EntityInfo> invalidEntityInfos = allEntityInfos.stream()
                .filter(entity -> !entity.hasTableAnnotation())
                .collect(Collectors.toList());

        // Ordenar entidades válidas por esquema y nombre de tabla
        validEntityInfos.sort(Comparator
                .comparing(EntityInfo::getSchema)
                .thenComparing(EntityInfo::getTableName));

        // Generar reportes en diferentes formatos
        consoleExporter.export(validEntityInfos, invalidEntityInfos);
        csvExporter.export(validEntityInfos, invalidEntityInfos, sourcePath);
        htmlExporter.export(validEntityInfos, invalidEntityInfos, sourcePath);
    }
}