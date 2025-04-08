package ru.ancap.framework.artifex.exception.operator;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.exception.exception.UnknownSubCommandException;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.Part;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.command.api.commands.exception.CommandErrorText;

public class NoSubCommandExceptionOperator implements CommandExceptionOperator<UnknownSubCommandException> {
    
    @Override
    public void on(UnknownSubCommandException exception, CommandSource source, LeveledCommand leveledCommand) {
        CommandErrorText.send(source.sender(), new LAPIText(
            Artifex.class, "command.api.error.unknown.no-subcommand",
            new Placeholder("command", "/"+ String.join(" ", leveledCommand.parts().subList(0, exception.lastIndex() + 1).stream()
                .map(Part::original).toList())),
            new Placeholder("sub", exception.unknownSubCommand())
        ));
    }
    
}