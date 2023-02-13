package ru.ancap.framework.command.api.commands.operator.arguments.bundle;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ArgumentsMap implements ArgumentsBundle {
    
    private final Map<String, Object> arguments;
    
    @Override
    public <T> T get(String name, Class<T> defineAs) {
        return (T) arguments.get(name);
    }
}
