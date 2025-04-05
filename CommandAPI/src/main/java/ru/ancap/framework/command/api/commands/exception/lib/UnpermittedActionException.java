package ru.ancap.framework.command.api.commands.exception.lib;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.communicate.message.CallableMessage;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class UnpermittedActionException extends RuntimeException {
    
    private final @Nullable CallableMessage actionDescription;
    private final @Nullable CallableMessage requiredPermission;
    
}