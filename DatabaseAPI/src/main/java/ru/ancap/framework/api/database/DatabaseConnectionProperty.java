package ru.ancap.framework.api.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ancap.framework.api.reader.DatabaseType;

@AllArgsConstructor
@Data
public class DatabaseConnectionProperty {
    
    private final String domain;
    private final String driverClass;
    private final DatabaseType databaseType;

    public DatabaseDriverProperty drivers() {
        return new DatabaseDriverProperty(domain, driverClass);
    }

}
