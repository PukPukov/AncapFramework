package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide;

import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;

public interface CommandProvidePattern {

    CSCommandOperator delegated();

    default CSCommand convert(CSCommand command) {
        return command;
    }

}
