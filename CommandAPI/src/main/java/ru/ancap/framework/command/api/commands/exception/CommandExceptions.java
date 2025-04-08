package ru.ancap.framework.command.api.commands.exception;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.command.api.commands.exception.exception.UnpermittedActionException;
import ru.ancap.framework.command.api.commands.flag.exception.FlagDataNotProvidedException;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.Optional;
import java.util.function.Supplier;

public class CommandExceptions {
    
    // INSUFFICIENT ARGUMENTS
    public static Supplier<NoNextArgumentException> noArgument(Supplier<CallableText> specificArgument) {
        return () -> new NoNextArgumentException(specificArgument.get());
    }
    
    // FLAG DATA
    public static Supplier<FlagDataNotProvidedException> noFlagData(String flagKey) {
        return () -> new FlagDataNotProvidedException(flagKey, Optional.empty());
    }
    
    public static Supplier<FlagDataNotProvidedException> noFlagData(String flagKey, CallableText dataDescription) {
        return () -> new FlagDataNotProvidedException(flagKey, Optional.of(dataDescription));
    }
    
    // PERMS
    public static Supplier<UnpermittedActionException> noPerms(@Nullable Supplier<CallableText> actionDescription, @Nullable Supplier<CallableText> requiredPermission) {
        return () -> new UnpermittedActionException(
            actionDescription != null ? actionDescription.get() : null,
            requiredPermission != null ? requiredPermission.get() : null
        );
    }
    
    public static Supplier<CallableText> bukkitPerm(String permission) {
        return () -> new LAPIText(
            CommonMessageDomains.Error.bukkitPermission,
            new Placeholder("permission", permission)
        );
    }
    
}