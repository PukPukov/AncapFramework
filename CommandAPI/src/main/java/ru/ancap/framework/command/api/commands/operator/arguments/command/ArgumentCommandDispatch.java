package ru.ancap.framework.command.api.commands.operator.arguments.command;

import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsBundle;

public record ArgumentCommandDispatch(CommandSource source, ArgumentsBundle arguments) {}
