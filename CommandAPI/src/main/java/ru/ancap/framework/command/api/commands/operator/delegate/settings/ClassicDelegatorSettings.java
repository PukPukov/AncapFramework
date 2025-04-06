package ru.ancap.framework.command.api.commands.operator.delegate.settings;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.exception.lib.UnknownCommandException;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;

@ToString @EqualsAndHashCode
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandOperator unknown = dispatch -> {
        boolean raw = dispatch.command().isRaw();
        throw new UnknownCommandException(raw ? "" : dispatch.command().currentPart().main(), raw); // TODO
    };
    
    public CommandOperator unknown() {
        return this.unknown;
    }
    
    @Override
    public CommandProvidePattern getDefaultRule() {
        return new CommandProvidePattern() {
            @Override
            public CommandOperator delegated() {
                return ClassicDelegatorSettings.this.unknown;
            }
            
            @Override
            public LeveledCommand convert(LeveledCommand command) {
                return command;
            }
        };
    }
    
}