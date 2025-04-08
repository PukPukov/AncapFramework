package ru.ancap.framework.artifex.exception.operator;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.flag.exception.FlagDataNotProvidedException;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.command.api.commands.exception.CommandErrorText;

public class FlagDataNotProvidedExceptionOperator implements CommandExceptionOperator<FlagDataNotProvidedException> {
    
    @Override
    public void on(FlagDataNotProvidedException exception, CommandSource source, LeveledCommand leveledCommand) {
        CallableText message;
        if (exception.dataDescription().isEmpty()) message = new LAPIText(
            Artifex.class, "command.api.error.flag-data-not-provided",
            new Placeholder("flag", exception.flagKey())
        );
        else message = new LAPIText(
            Artifex.class, "command.api.error.flag-data-not-provided-explained",
            new Placeholder("flag", exception.flagKey()),
            new Placeholder("data", exception.dataDescription().get())
        );
        CommandErrorText.send(source.sender(), message);
    }
    
}