package ru.ancap.framework.command.api.commands.operator.delegate.subcommand;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;

import java.util.List;

public class Raw implements CommandDelegateRule {

    private final CommandOperator delegated;

    public Raw(CommandOperator delegated) {
        this.delegated = delegated;
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        return command.isRaw();
    }

    @Override
    public CommandOperator delegated() {
        return this.delegated;
    }

    @Override
    public List<String> candidates() {
        return List.of();
    }
}
