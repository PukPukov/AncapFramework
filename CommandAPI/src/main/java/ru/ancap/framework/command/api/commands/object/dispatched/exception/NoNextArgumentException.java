package ru.ancap.framework.command.api.commands.object.dispatched.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableText;

import java.util.Optional;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class NoNextArgumentException extends RuntimeException {

    private final Optional<CallableText> argumentDescription;
    
    public NoNextArgumentException(CallableText argumentDescription) {
        this(Optional.of(argumentDescription));
    }
    
    public NoNextArgumentException() {
        this(Optional.empty());
    }
    
}