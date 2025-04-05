package ru.ancap.framework.plugin.api.commands.exception;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.command.api.commands.exception.lib.NoSpecificArgumentException;
import ru.ancap.framework.command.api.commands.exception.lib.UnpermittedActionException;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.function.Supplier;

public class CommandExceptions {
    
    // INSUFFICIENT ARGUMENTS
    public static Supplier<NoSpecificArgumentException> noArgument(Supplier<CallableMessage> specificArgument) {
        return () -> new NoSpecificArgumentException(specificArgument.get());
    }
    
    // PERMS
    public static Supplier<UnpermittedActionException> noPerms(@Nullable Supplier<CallableMessage> actionDescription, @Nullable Supplier<CallableMessage> requiredPermission) {
        return () -> new UnpermittedActionException(
            actionDescription != null ? actionDescription.get() : null,
            requiredPermission != null ? requiredPermission.get() : null
        );
    }
    
    public static Supplier<CallableMessage> bukkitPerm(String permission) {
        return () -> new LAPIMessage(
            CommonMessageDomains.Error.bukkitPermission,
            new Placeholder("permission", permission)
        );
    }
    
}