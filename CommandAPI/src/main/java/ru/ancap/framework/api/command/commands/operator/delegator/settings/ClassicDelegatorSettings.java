package ru.ancap.framework.api.command.commands.operator.delegator.settings;

import lombok.Getter;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;
import ru.ancap.framework.api.command.commands.operator.finite.FiniteCommandTarget;
import ru.ancap.framework.api.command.commands.operator.finite.pattern.SingleArgumenter;
import ru.ancap.framework.api.event.classic.UnknownCommandEvent;

/**
 * It's a delegator settings class that uses the `UnknownCommandEvent` as the default rule
 */
@Getter
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandProvidePattern spareRule;
    private final CommandOperator unknown = new FiniteCommandTarget(
            new SingleArgumenter(UnknownCommandEvent::new)
    );

    public ClassicDelegatorSettings() {
        this.spareRule = () -> unknown;
    }

    @Override
    public CommandProvidePattern getDefaultRule() {
        return spareRule;
    }
}
