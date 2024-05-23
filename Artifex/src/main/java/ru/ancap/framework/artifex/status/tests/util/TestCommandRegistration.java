package ru.ancap.framework.artifex.status.tests.util;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;

import java.util.List;

public class TestCommandRegistration {
    
    public static CloseableCommandRegistration register(PluginCommandRegistrar baseRegistrar, String key, CommandOperator operator) {
        baseRegistrar.register(key, operator);
        return new CloseableCommandRegistration(baseRegistrar, key);
    }
    
    public static CloseableCommandRegistration register(PluginCommandRegistrar baseRegistrar, String key, List<String> aliases, CommandOperator operator) {
        baseRegistrar.register(key, aliases, operator);
        return new CloseableCommandRegistration(baseRegistrar, key);
    }
    
    @ToString @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class CloseableCommandRegistration implements AutoCloseable {
        
        private final PluginCommandRegistrar baseRegistrar;
        private final String key;
        
        @Override
        public void close() {
            this.baseRegistrar.unregister(this.key);
        }
        
    }
    
}