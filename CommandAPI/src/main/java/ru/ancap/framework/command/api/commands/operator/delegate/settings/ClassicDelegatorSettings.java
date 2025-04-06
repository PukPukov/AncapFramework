package ru.ancap.framework.command.api.commands.operator.delegate.settings;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.exception.lib.UnknownCommandException;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;

@ToString @EqualsAndHashCode
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandProvidePattern spareRule;
    private final CommandOperator unknown = dispatch -> {
        throw new UnknownCommandException(dispatch.command().nextArgument());
    };
    
    public CommandProvidePattern spareRule() {
        return this.spareRule;
    }
    
    public CommandOperator unknown() {
        return this.unknown;
    }

    public ClassicDelegatorSettings() {
        this.spareRule = () -> unknown;
    }

    @Override
    public CommandProvidePattern getDefaultRule() {
        return spareRule;
    }
}