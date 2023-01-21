package ru.ancap.framework.api.reader;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.database.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
public class ConfigDatabaseLoader implements DatabaseLoader {

    private final Plugin plugin;
    private final Map<String, DatabaseConnectionProperty> dbsProperties;
    private final ConfigurationSection connectionConfig;
    private final Consumer<InitializationContext> consumer;

    // В будущем надо бы переписать это говно, а пока что и так сойдёт
    // А может и не надо, лучше буду хибер юзать (и вам советую), ещё даже подключение к базе данных не написал, а скл уже заебал
    @Override
    public LoadResult load() {
        @NotNull String databaseTypeName = connectionConfig.getString("type");
        @NotNull DatabaseConnectionProperty property = dbsProperties.get(databaseTypeName);
        DatabaseDriverProperty drivers = property.drivers();
        try {
            Class.forName(property.driverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ConnectionProvider provider = switch (property.type()) {
            case FILE -> {
                String dbName = connectionConfig.getString("file.name");
                File file = new File(plugin.getDataFolder() + File.separator + dbName);
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                yield new LocalConnectionProvider(drivers.domain(), file);
            }
            case REMOTE -> {
                String host = connectionConfig.getString("remote.host");
                String databaseName = connectionConfig.getString("remote.name");
                String user = connectionConfig.getString("remote.user");
                String password = connectionConfig.getString("remote.password");
                int port = connectionConfig.getInt("remote.port");
                yield new RemoteConnectionProvider(
                        new RemoteConnectionProvider.Domain(drivers.domain()),
                        new RemoteConnectionProvider.Host(host),
                        new RemoteConnectionProvider.Port(port),
                        new RemoteConnectionProvider.DatabaseName(databaseName),
                        new RemoteConnectionProvider.User(user),
                        new RemoteConnectionProvider.Password(password)
                );
            }
        };
        boolean initialize = true;
        boolean setInitialized = false;
        try {
            Connection connection = provider.get();
            String query = """
                    CREATE TABLE IF NOT EXISTS AncapDatabaseProperties (
                                                property VARCHAR(255) PRIMARY KEY,
                                                value INTEGER
                    );
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.execute();
            }
            query = """
                    SELECT property, value FROM AncapDatabaseProperties 
                    WHERE property = 'initialized';
                    """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        if (resultSet.getInt("value") == 1) {
                            initialize = false;
                        }
                    } else {
                        setInitialized = true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (initialize) {
            consumer.accept(new InitializationContext(provider.get()));
        }
        if (setInitialized) {
            String  query = """
                            INSERT INTO AncapDatabaseProperties (property, value) VALUES ("initialized", 1);
                            """;
            try (
                    PreparedStatement statement = provider
                    .get()
                    .prepareStatement(query)
            ) {
                statement.execute();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
        return () -> provider;
    }
}
