package co.g3a.entityscanner;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "entity-scanner")
public class EntityScannerConfig {
    
    private String outputDirectory = "output";
    private String dateFormat = "yyyyMMdd_HHmmss";
    private boolean enableConsoleReport = true;
    private boolean enableCsvReport = true;
    private boolean enableHtmlReport = true;
    
    // Getters y setters
    public String getOutputDirectory() {
        return outputDirectory;
    }
    
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
    
    public String getDateFormat() {
        return dateFormat;
    }
    
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    
    public boolean isEnableConsoleReport() {
        return enableConsoleReport;
    }
    
    public void setEnableConsoleReport(boolean enableConsoleReport) {
        this.enableConsoleReport = enableConsoleReport;
    }
    
    public boolean isEnableCsvReport() {
        return enableCsvReport;
    }
    
    public void setEnableCsvReport(boolean enableCsvReport) {
        this.enableCsvReport = enableCsvReport;
    }
    
    public boolean isEnableHtmlReport() {
        return enableHtmlReport;
    }
    
    public void setEnableHtmlReport(boolean enableHtmlReport) {
        this.enableHtmlReport = enableHtmlReport;
    }
}