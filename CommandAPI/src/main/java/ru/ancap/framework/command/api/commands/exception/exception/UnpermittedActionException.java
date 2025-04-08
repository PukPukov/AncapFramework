package ru.ancap.framework.command.api.commands.exception.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.message.CallableText;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class UnpermittedActionException extends RuntimeException {
    
    private final @Nullable CallableText actionDescription;
    private final @Nullable CallableText requiredPermission;
    
}