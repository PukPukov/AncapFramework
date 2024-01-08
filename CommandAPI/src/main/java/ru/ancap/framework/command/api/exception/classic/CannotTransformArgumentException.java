package ru.ancap.framework.command.api.exception.classic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.ancap.framework.command.api.exception.exceptions.UserError;

import java.util.List;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class CannotTransformArgumentException extends UserError {
    
    private final List<String> argument;
    private final Class<?> requiredType;
    
}
