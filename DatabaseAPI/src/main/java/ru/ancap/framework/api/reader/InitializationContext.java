package ru.ancap.framework.api.reader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Connection;

@AllArgsConstructor
@Data
public class InitializationContext {
    private final Connection connection;
}
