package co.g3a.entityscanner;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {
    
    /**
     * Escapa caracteres especiales para HTML
     */
    public String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    /**
     * Escapa caracteres especiales para CSV
     */
    public String escapeCsv(String value) {
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