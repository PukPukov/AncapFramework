package ru.ancap.framework.api.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import ru.ancap.framework.api.function.Holder;
import ru.ancap.framework.api.function.InitializedHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@AllArgsConstructor
public class RemoteConnectionProvider implements ConnectionProvider {

    private final Holder<Connection> holder = new InitializedHolder<>();
    private final Domain domain;
    private final Host host;
    private final Port port;
    private final DatabaseName databaseName;
    private final User user;
    private final Password password;

    @Override
    @SneakyThrows
    public Connection get() {
        return holder.get(() -> {
            try {
                return DriverManager.getConnection(
                        "jdbc:"+domain.domain+"://"+host.host+":"+port.port+"/"+databaseName.name+"?useUnicode=true&characterEncoding=utf8&autoReconnect=true",
                        user.name,
                        password.password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AllArgsConstructor
    @Data
    public static class Domain {
        private final String domain;
    }
    
    @AllArgsConstructor
    @Data
    public static class Host {
        private final String host;
    }
    
    @AllArgsConstructor
    @Data
    public static class Port {
        private final int port;
    }

    @AllArgsConstructor
    @Data
    public static class DatabaseName {
        private final String name;
    }

    @AllArgsConstructor
    @Data
    public static class User {
        private final String name;
    }

    @AllArgsConstructor
    @Data
    public static class Password {
        private final String password;
    }
    
}
