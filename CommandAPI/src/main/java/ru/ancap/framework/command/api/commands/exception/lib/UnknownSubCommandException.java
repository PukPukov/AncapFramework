package ru.ancap.framework.command.api.commands.exception.lib;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class UnknownSubCommandException extends RuntimeException {
    
    private final String unknownSubCommand;
    private final int lastIndex;
    
}