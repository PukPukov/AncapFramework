package ru.ancap.framework.command.api.commands.exception.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class UnknownSubCommandException extends RuntimeException {
    
    private final String unknownSubCommand;
    private final int lastIndex;
    
}