package ru.ancap.framework.command.api.commands.operator.delegate.subcommand;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;

import java.util.List;

import static ru.ancap.commons.debug.AncapDebug.*;

public record Raw(CommandOperator delegated) implements CommandDelegateRule {

    @Override
    public boolean isOperate(LeveledCommand command) {
        return debugThrough(command.isRaw());
    }

    @Override
    public List<String> candidates() {
        return List.of();
    }
    
    @Override
    public LeveledCommand convert(LeveledCommand command) {
        return command;
    }
    
}