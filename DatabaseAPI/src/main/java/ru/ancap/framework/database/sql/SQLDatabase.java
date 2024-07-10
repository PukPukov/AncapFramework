package ru.ancap.framework.database.sql;

import com.j256.ormlite.support.ConnectionSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ancap.framework.database.sql.connection.data.DatabaseInfo;

import javax.sql.DataSource;

@AllArgsConstructor
@Accessors(fluent = true) @Getter
public class SQLDatabase {
    
    private final ConnectionSource orm;
    private final DataSource dataSource;
    
    private final DatabaseInfo info;
    
}