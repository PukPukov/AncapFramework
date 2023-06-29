package ru.ancap.framework.command.api.commands.operator.delegate.subcommand;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.DelegatePattern;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;

import java.util.List;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class SubCommand implements CommandDelegateRule {

    private final OperateRule operateRule;
    private final List<String> candidates;
    private final CommandOperator delegated;
    
    public SubCommand(String delegatePattern, CommandOperator executor) {
        this(new StringDelegatePattern(delegatePattern), executor);
    }

    public SubCommand(DelegatePattern delegatePattern, CommandOperator executor) {
        this(delegatePattern, delegatePattern.candidates(), executor);
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        return this.operateRule.isOperate(command);
    }

    @Override
    public CommandOperator delegated() {
        return this.delegated;
    }

    @Override
    public LeveledCommand convert(LeveledCommand command) {
        return command.withoutArgument();
    }

    @Override
    public List<String> candidates() {
        return candidates;
    }
}
