package ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.provide;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;

public interface CommandProvidePattern {

    CommandOperator delegated();

    default CommandSender convert(CommandSender sender) {
        return sender;
    }

    default LeveledCommand convert(LeveledCommand command) {
        return command;
    }

}
