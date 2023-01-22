package ru.ancap.framework.api.command.commands.operator.delegator.subcommand;

import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.CommandDelegateRule;
import ru.ancap.framework.api.command.commands.operator.finite.FiniteCommandTarget;
import ru.ancap.framework.api.command.commands.operator.finite.pattern.ArgumentCutter;

import java.util.List;

public class Raw implements CommandDelegateRule {

    private final CommandOperator delegated;

    public Raw(CommandOperator delegated) {
        this.delegated = delegated;
    }

    public Raw(ArgumentCutter.SenderEventProvider provider) {
        this.delegated = new FiniteCommandTarget(
                new ArgumentCutter(provider)
        );
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
