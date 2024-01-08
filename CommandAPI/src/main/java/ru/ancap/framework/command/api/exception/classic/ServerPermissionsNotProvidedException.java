package ru.ancap.framework.command.api.exception.classic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.ancap.framework.command.api.exception.exceptions.UserError;

/**
 * Thrown, when players tries to perform actions, that require granted by server
 * permissions, e.g. when non-moderator tries to /ban. This is unrelated to 
 * gameplay restrictions like disability of non-mayor to delete city in Towny.
 * In that case more specific exception should be used. 
 */
@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class ServerPermissionsNotProvidedException extends UserError {
    
    private final String permission;
    
}
