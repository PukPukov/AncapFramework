package ru.ancap.framework.database.sql.connection.data;

import com.j256.ormlite.db.BaseDatabaseType;

public record DatabaseInfo(String domain, String driverClass, BaseDatabaseType databaseType, DatabaseLocation databaseLocation) {}
