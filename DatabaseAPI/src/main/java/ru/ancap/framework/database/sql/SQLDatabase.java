package ru.ancap.framework.database.sql;

import com.j256.ormlite.support.ConnectionSource;
import lombok.AllArgsConstructor;
import ru.ancap.framework.database.sql.connection.data.DatabaseInfo;

import javax.sql.DataSource;

@AllArgsConstructor
public class SQLDatabase {

    private final ConnectionSource connectionSource;
    private final DataSource dataSource;
    
    private final DatabaseInfo connectionData;
    
    public ConnectionSource orm        () { return this.connectionSource; }
    public DataSource       dataSource () { return this.dataSource;       }
    public DatabaseInfo     info       () { return this.connectionData;   }
    
    
}
