package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

public interface CommandProvidePattern {

    CommandOperator delegated();
    LeveledCommand convert(LeveledCommand command);

}