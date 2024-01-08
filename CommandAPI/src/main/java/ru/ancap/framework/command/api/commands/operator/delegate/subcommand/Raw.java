package ru.ancap.framework.command.api.commands.operator.delegate.subcommand;

import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;

import java.util.List;

public record Raw(CSCommandOperator delegated) implements CommandDelegateRule {

    @Override
    public boolean isOperate(CSCommand command) {
        return command.isRaw();
    }

    @Override
    public List<String> candidates() {
        return List.of();
    }
    
}
