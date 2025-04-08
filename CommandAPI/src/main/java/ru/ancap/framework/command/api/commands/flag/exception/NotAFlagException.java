package ru.ancap.framework.command.api.commands.flag.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class NotAFlagException extends RuntimeException {
    
    private final String providedString;
    
}