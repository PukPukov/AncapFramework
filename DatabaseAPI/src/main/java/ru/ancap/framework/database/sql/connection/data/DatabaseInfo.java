package ru.ancap.framework.database.sql.connection.data;

import com.j256.ormlite.db.BaseDatabaseType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseInfo {
    
    private final String domain;
    private final String driverClass;
    private final BaseDatabaseType databaseType;
    private final DatabaseLocation databaseLocation;
    
    public String domain() {
        return this.domain;
    }
    
    public String driverClass() {
        return this.driverClass;
    }
    
    public BaseDatabaseType databaseType() {
        return this.databaseType;
    }
    
    public DatabaseLocation databaseLocation() {
        return this.databaseLocation;
    }

}
