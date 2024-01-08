package ru.ancap.framework.command.api.exception.classic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.ancap.framework.command.api.exception.exceptions.UserError;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class RequiredArgumentMissingException extends UserError {

    private final Class<?> requiredType;
    
}
