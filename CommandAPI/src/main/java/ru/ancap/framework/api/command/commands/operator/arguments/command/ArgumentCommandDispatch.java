package ru.ancap.framework.api.command.commands.operator.arguments.command;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.operator.arguments.bundle.ArgumentsBundle;

public record ArgumentCommandDispatch (
        CommandSender sender,
        ArgumentsBundle arguments
) {}
