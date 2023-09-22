package ru.ancap.framework.command.api.commands.object.event;

import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

public record CommandDispatch(CommandSource source, LeveledCommand command) {}
