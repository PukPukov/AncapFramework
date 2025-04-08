package ru.ancap.framework.artifex.exception.operator;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.command.api.commands.exception.CommandErrorText;

public class NoNextArgumentExceptionOperator implements CommandExceptionOperator<NoNextArgumentException> {
    
    @Override
    public void on(NoNextArgumentException exception, CommandSource source, LeveledCommand leveledCommand) {
        CallableText message;
        if (exception.argumentDescription().isEmpty()) message = new LAPIText(Artifex.class, "command.api.error.expected-part");
        else message = new LAPIText(
            Artifex.class, "command.api.error.expected-typed-part",
            new Placeholder("part", exception.argumentDescription())
        );
        CommandErrorText.send(source.sender(), message);
    }
    
}