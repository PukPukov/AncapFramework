package ru.ancap.framework.command.api.commands.flag.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableText;

import java.util.Optional;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString @EqualsAndHashCode(callSuper = true)
public class FlagDataNotProvidedException extends RuntimeException {
    
    private final String flagKey;
    private final Optional<CallableText> dataDescription;
    
}