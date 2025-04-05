package ru.ancap.framework.command.api.commands.exception.lib;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableMessage;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class NoSpecificArgumentException extends RuntimeException {
    
    private final CallableMessage argumentDescription;
    
}