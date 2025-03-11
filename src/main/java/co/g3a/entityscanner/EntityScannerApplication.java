package co.g3a.entityscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class EntityScannerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EntityScannerApplication.class);
        app.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EntityScanner scanner, ReportGenerator reportGenerator) {
        return args -> {
            if (args.length == 0) {
                System.err.println("Por favor, proporcione la ruta de la base de código como argumento.");
                System.err.println("Forma de uso: java -jar target/entity-scanner-0.0.1-SNAPSHOT.jar you_path_project/src/main/java");
                System.exit(1);
                return;
            }

            String sourcePath = args[0];
            System.out.println("Escaneando entidades en: " + sourcePath);

            long startTime = System.currentTimeMillis();
            List<EntityInfo> allEntityInfos = scanner.scanJavaFiles(sourcePath);
            long scanTime = System.currentTimeMillis() - startTime;

            System.out.println("Escaneo completado en " + scanTime + "ms. Encontradas " +
                    allEntityInfos.size() + " entidades.");

            startTime = System.currentTimeMillis();
            reportGenerator.generateReports(allEntityInfos, sourcePath);
            long reportTime = System.currentTimeMillis() - startTime;

            System.out.println("Reportes generados en " + reportTime + "ms.");
            System.exit(0);
        };
    }
}