package ru.ancap.framework.database.sql.connection.reader;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.jdbc.db.*;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import ru.ancap.framework.database.sql.SQLDatabase;
import ru.ancap.framework.database.sql.connection.data.DatabaseInfo;
import ru.ancap.framework.database.sql.connection.data.DatabaseLocation;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RequiredArgsConstructor
public class DatabaseFromConfig {
    
    private final Map<String, DatabaseInfo> templates = Map.ofEntries(
            Map.entry("sqlite",       new DatabaseInfo("sqlite",     "org.sqlite.JDBC",                      new SqliteDatabaseType(),            DatabaseLocation.LOCAL)),
            Map.entry("mysql",        new DatabaseInfo("mysql",      "com.mysql.cj.jdbc.Driver",             new MysqlDatabaseType(),             DatabaseLocation.PUBLIC)),
            Map.entry("maria-db",     new DatabaseInfo("mariadb",    "org.mariadb.jdbc.Driver",              new MariaDbDatabaseType(),           DatabaseLocation.PUBLIC)),
            Map.entry("postgresql",   new DatabaseInfo("postgresql", "org.postgresql.Driver",                new PostgresDatabaseType(),          DatabaseLocation.PUBLIC)),
            Map.entry("oracle",       new DatabaseInfo("oracle",     "oracle.jdbc.driver.OracleDriver",      new OracleDatabaseType(),            DatabaseLocation.PUBLIC)),
            Map.entry("derby-local",  new DatabaseInfo("derby",      "org.apache.derby.jdbc.EmbeddedDriver", new DerbyEmbeddedDatabaseType(),     DatabaseLocation.LOCAL)),
            Map.entry("derby-server", new DatabaseInfo("derby",      "org.apache.derby.jdbc.ClientDriver",   new DerbyClientServerDatabaseType(), DatabaseLocation.PUBLIC)),
            Map.entry("hsqldb",       new DatabaseInfo("hsqldb",     "org.hsqldb.jdbcDriver",                new HsqldbDatabaseType(),            DatabaseLocation.PUBLIC)),
            Map.entry("netezza",      new DatabaseInfo("netezza",    "org.netezza.Driver",                   new NetezzaDatabaseType(),           DatabaseLocation.PUBLIC)),
            Map.entry("odbc",         new DatabaseInfo("odbc",       "sun.jdbc.odbc.JdbcOdbcDriver",         new GenericOdbcDatabaseType(),       DatabaseLocation.PUBLIC))
    );

    private final Plugin plugin;
    private final ConfigurationSection connectionConfig;

    public SQLDatabase load() {
        new GenericOdbcDatabaseType();
        new NetezzaDatabaseType();
        new SqlServerJtdsDatabaseType();
        
        @NonNull String databaseTypeName = this.connectionConfig.getString("type");
        DatabaseInfo info = this.templates.get(databaseTypeName);
        info.databaseType().loadDriver();
        HikariConfig config = new HikariConfig();
        
        switch (info.databaseLocation()) {
            case LOCAL:
                
                String dbName = connectionConfig.getString("file.name");
                File file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + dbName);
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                
                config.setJdbcUrl("jdbc:"+info.domain()+"://"+file.getAbsolutePath());
                config.setUsername("");
                config.setPassword("");
                break;
                
            case PUBLIC:
                
                String host = connectionConfig.getString("remote.host");
                String databaseName = connectionConfig.getString("remote.name");
                String user = connectionConfig.getString("remote.user");
                String password = connectionConfig.getString("remote.password");
                int port = connectionConfig.getInt("remote.port");

                config.setJdbcUrl("jdbc:"+info.domain()+"://"+host+":"+port+"/"+databaseName);
                config.setUsername(user);
                config.setPassword(password);
                break;
                
            default: throw new IllegalStateException("Unexpected value: " + info.databaseLocation());
        }
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        DataSource dataSource = new HikariDataSource(config);
        
        ConnectionSource connectionSource;
        try {
            connectionSource = new DataSourceConnectionSource(dataSource, info.databaseType());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new SQLDatabase(connectionSource, dataSource, info);
    }

}