package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

public interface CommandProvidePattern {

    CommandOperator delegated();

    default LeveledCommand convert(LeveledCommand command) {
        return command;
    }

}
