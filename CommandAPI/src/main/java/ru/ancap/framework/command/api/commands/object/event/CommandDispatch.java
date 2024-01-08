package ru.ancap.framework.command.api.commands.object.event;

import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.syntax.CSCommand;

public record CommandDispatch(CommandSource source, CSCommand command) implements CommandEvent {}
