package ru.ancap.framework.artifex.exception.operator;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.exception.exception.UnawaitedRawCommandException;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.command.api.commands.exception.CommandErrorText;

public class UnawaitedRawCommandExceptionOperator implements CommandExceptionOperator<UnawaitedRawCommandException> {
    
    @Override
    public void on(UnawaitedRawCommandException exception, CommandSource source, LeveledCommand leveledCommand) {
        CommandErrorText.send(source.sender(), new LAPIText(
            Artifex.class, "command.api.error.unknown.raw",
            new Placeholder("command", "/"+leveledCommand.original())
        ));
    }
    
}