package ru.ancap.framework.api.database;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DatabaseDriverProperty {
    
    private final String domain;
    private final String driverClass;

}
