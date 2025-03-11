package co.g3a.entityscanner;

import lombok.Getter;

public class EntityInfo {
    @Getter
    private final String className;
    @Getter
    private final String packageName;
    @Getter
    private final String filePath;
    @Getter
    private final String tableName;
    @Getter
    private final String schema;
    private final boolean hasTableAnnotation;

    public EntityInfo(String className, String packageName, String filePath, 
                      String tableName, String schema, boolean hasTableAnnotation) {
        this.className = className;
        this.packageName = packageName;
        this.filePath = filePath;
        this.tableName = tableName;
        this.schema = schema;
        this.hasTableAnnotation = hasTableAnnotation;
    }

    public boolean hasTableAnnotation() {
        return hasTableAnnotation;
    }
}