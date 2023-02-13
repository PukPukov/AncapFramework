package ru.ancap.framework.database.sql;

import com.j256.ormlite.support.ConnectionSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ancap.framework.database.sql.connection.data.DatabaseInfo;

import javax.sql.DataSource;

@AllArgsConstructor
@Data
public class SQLDatabase {

    private final ConnectionSource connectionSource;
    private final DataSource dataSource;
    
    private final DatabaseInfo connectionData;
    
}
