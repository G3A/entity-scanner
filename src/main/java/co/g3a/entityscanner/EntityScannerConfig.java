package co.g3a.entityscanner;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "entity-scanner")
@Getter
@Setter
public class EntityScannerConfig {
    
    private String outputDirectory = "output";
    private String dateFormat = "yyyyMMdd_HHmmss";
    private boolean enableConsoleReport = true;
    private boolean enableCsvReport = true;
    private boolean enableHtmlReport = true;
}