# Entity Scanner

¿Cansado de buscar manualmente todas las tablas que usa tu aplicación? 😫  
¿Frustrado intentando descifrar qué esquemas de base de datos necesitas para ese nuevo ambiente? 🤔  
¿Te aterra la idea de revisar cientos de archivos Java solo para encontrar las entidades JPA? 😱

**¡Entity Scanner es tu solución!** 🚀

Con un simple comando, esta herramienta mágica:
- 📊 Escanea todo tu código fuente
- 🎯 Identifica todas las entidades JPA
- 📋 Genera reportes detallados de esquemas y tablas
- 🎨 Presenta la información en formatos fáciles de leer (HTML, CSV y consola)

¡Olvídate de perder horas revisando código manualmente! Entity Scanner hace el trabajo pesado por ti en segundos.


Entity Scanner es una herramienta de línea de comandos desarrollada bajo el paraguas de Spring Boot que analiza código fuente Java
para identificar y generar reportes sobre entidades JPA en un proyecto.

## Descripción

La herramienta escanea archivos Java en busca de entidades JPA, identificando específicamente aquellas que tienen la anotación `@Table`. Genera reportes en múltiples formatos (consola, CSV y HTML) con información detallada sobre las entidades encontradas.

## Requisitos Previos

- Java 21
- Maven 3.6.x o superior
- Git (opcional)

## Configuración del Ambiente de Desarrollo

1. Clonar el repositorio (o descargar el código fuente):
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd entity-scanner
   ```

2. Compilar el proyecto:
   ```bash
   mvn clean compile
   ```

## Empaquetado

Para generar el archivo JAR ejecutable:

```bash
mvn clean package
```

Esto generará el archivo `entity-scanner-0.0.1-SNAPSHOT.jar` en el directorio `target/`.

## Uso

Para ejecutar la herramienta:

```bash
java -jar target/entity-scanner-0.0.1-SNAPSHOT.jar [RUTA_DEL_CODIGO_FUENTE]
```

Por ejemplo:
```bash
java -jar target/entity-scanner-0.0.1-SNAPSHOT.jar /ruta/a/tu/proyecto/src/main/java
```

### Configuración

La aplicación puede ser configurada mediante el archivo `application.properties` con las siguientes propiedades:

```properties
entity-scanner.output-directory=output
entity-scanner.date-format=yyyyMMdd_HHmmss
entity-scanner.enable-console-report=true
entity-scanner.enable-csv-report=true
entity-scanner.enable-html-report=true
```

### Salida

La herramienta generará:

1. Un reporte en consola mostrando las entidades encontradas
2. Un archivo CSV con el detalle de las entidades
3. Un reporte HTML con la información formateada

Los reportes incluirán:
- Entidades válidas (con anotación @Table)
- Entidades inválidas (sin anotación @Table)
- Información sobre esquemas y nombres de tablas
- Ruta del archivo fuente

Los archivos de reporte se guardarán en el directorio especificado en la configuración (por defecto: `output/`).

## Características

- Escaneo recursivo de archivos Java
- Identificación de entidades JPA
- Múltiples formatos de reporte (Consola, CSV, HTML)
- Clasificación de entidades por esquema y nombre de tabla
- Identificación de entidades sin anotación @Table

## Desarrollo

El proyecto está estructurado usando Spring Boot y utiliza las siguientes dependencias principales:

- Spring Boot 3.4.3
- Lombok para reducción de código boilerplate
- Maven como sistema de construcción

## Licencia

[Mit license](https://opensource.org/license/mit)
