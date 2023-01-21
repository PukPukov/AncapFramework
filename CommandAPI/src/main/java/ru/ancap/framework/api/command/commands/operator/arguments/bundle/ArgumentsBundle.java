package ru.ancap.framework.api.command.commands.operator.arguments.bundle;

public interface ArgumentsBundle {
    
    <T> T get(String name, Class<T> defineAs);
    
}
