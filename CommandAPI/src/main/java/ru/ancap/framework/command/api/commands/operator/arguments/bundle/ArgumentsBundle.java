package ru.ancap.framework.command.api.commands.operator.arguments.bundle;

public interface ArgumentsBundle {
    
    <T> T get(String name, Class<T> defineAs);
    
}
