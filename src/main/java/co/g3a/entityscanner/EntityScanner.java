package co.g3a.entityscanner;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Lazy
public class EntityScanner {

    // Patrones precompilados
    private static final Pattern ENTITY_PATTERN = Pattern.compile("@Entity", Pattern.CASE_INSENSITIVE);
    private static final Pattern TABLE_ANNOTATION_PATTERN = Pattern.compile("@Table\\s*\\(([^)]*)\\)", Pattern.DOTALL);
    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([\\w\\.]+)\\s*;", Pattern.CASE_INSENSITIVE);
    private static final Pattern CLASS_PATTERN = Pattern.compile("(?:public|private|protected)?\\s+class\\s+([A-Z][A-Za-z0-9_$]*)\\s*(?:extends|implements|\\{|$)", Pattern.CASE_INSENSITIVE);
    private static final Pattern NAME_PATTERN = Pattern.compile("name\\s*=\\s*\"([^\"]*)\"");
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("schema\\s*=\\s*\"([^\"]*)\"");

    // Usar procesamiento paralelo
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() - 1));

    public List<EntityInfo> scanJavaFiles(String sourcePath) {
        List<Path> javaFiles = findJavaFiles(sourcePath);
        System.out.println("Encontrados " + javaFiles.size() + " archivos Java para analizar");

        // Crear tareas de procesamiento para cada archivo
        List<Future<EntityInfo>> futures = new ArrayList<>();
        for (Path file : javaFiles) {
            futures.add(executor.submit(() -> processJavaFile(file)));
        }

        // Recopilar resultados
        List<EntityInfo> entityInfos = new ArrayList<>();
        for (Future<EntityInfo> future : futures) {
            try {
                EntityInfo info = future.get();
                if (info != null) {
                    entityInfos.add(info);
                }
            } catch (Exception e) {
                // Ignorar archivos con errores
            }
        }

        // Apagar el executor
        executor.shutdown();
        return entityInfos;
    }

    private List<Path> findJavaFiles(String sourcePath) {
        List<Path> javaFiles = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(sourcePath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        javaFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error al buscar archivos Java: " + e.getMessage());
        }
        return javaFiles;
    }

    private EntityInfo processJavaFile(Path file) {
        try {
            String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            // Verificación rápida antes de aplicar regex más costosas
            if (!content.contains("@Entity")) {
                return null;
            }

            // Verificar si es una entidad
            Matcher entityMatcher = ENTITY_PATTERN.matcher(content);
            if (!entityMatcher.find()) {
                return null;
            }

            // Extraer información básica
            String packageName = extractPackageName(content);
            String className = extractClassName(content);

            // Extraer información de la anotación @Table si existe
            String schema = "default";
            String tableName = className;
            boolean hasTableAnnotation = false;

            Matcher tableMatcher = TABLE_ANNOTATION_PATTERN.matcher(content);
            if (tableMatcher.find()) {
                hasTableAnnotation = true;
                String tableAnnotation = tableMatcher.group(1);

                // Extraer nombre de la tabla
                Matcher nameMatcher = NAME_PATTERN.matcher(tableAnnotation);
                if (nameMatcher.find()) {
                    tableName = nameMatcher.group(1);
                }

                // Extraer esquema
                Matcher schemaMatcher = SCHEMA_PATTERN.matcher(tableAnnotation);
                if (schemaMatcher.find()) {
                    schema = schemaMatcher.group(1);
                }
            }

            // Crear la información de la entidad
            return new EntityInfo(className, packageName, file.toString(), tableName, schema, hasTableAnnotation);

        } catch (IOException e) {
            System.err.println("Error al procesar archivo " + file + ": " + e.getMessage());
            return null;
        }
    }

    private String extractPackageName(String content) {
        Matcher packageMatcher = PACKAGE_PATTERN.matcher(content);
        return packageMatcher.find() ? packageMatcher.group(1) : "";
    }

    private String extractClassName(String content) {
        Matcher classMatcher = CLASS_PATTERN.matcher(content);
        return classMatcher.find() ? classMatcher.group(1) : "";
    }
}