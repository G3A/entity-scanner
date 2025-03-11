package co.g3a.entityscanner;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConsoleReportExporter {

    public void export(List<EntityInfo> validEntities, List<EntityInfo> invalidEntities) {
        System.out.println("\n===== REPORTE DE ENTIDADES JPA =====");
        System.out.println("Total de entidades con @Table: " + validEntities.size());
        System.out.println("Total de entidades sin @Table: " + invalidEntities.size());
        
        // Agrupar por esquema para mostrar estadísticas
        Map<String, List<EntityInfo>> entitiesBySchema = validEntities.stream()
                .collect(Collectors.groupingBy(EntityInfo::getSchema));
                
        System.out.println("\n--- Resumen por Esquema ---");
        for (Map.Entry<String, List<EntityInfo>> entry : entitiesBySchema.entrySet()) {
            System.out.printf("Esquema '%s': %d tablas%n", 
                    entry.getKey(), entry.getValue().size());
        }
        
        // Imprimir listado detallado (opcional, podría limitarse a un número pequeño)
        System.out.println("\n--- Primeras 5 entidades encontradas ---");
        validEntities.stream().limit(5).forEach(entity -> 
            System.out.printf("Tabla: %s.%s (Clase: %s)%n", 
                entity.getSchema(), entity.getTableName(), entity.getClassName())
        );
    }
}