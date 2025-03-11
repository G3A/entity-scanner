package co.g3a.entityscanner;

public class EntityInfo {
    private final String className;
    private final String packageName;
    private final String filePath;
    private final String tableName;
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

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchema() {
        return schema;
    }

    public boolean hasTableAnnotation() {
        return hasTableAnnotation;
    }
}