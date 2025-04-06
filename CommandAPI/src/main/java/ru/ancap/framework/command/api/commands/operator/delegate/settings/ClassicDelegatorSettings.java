package ru.ancap.framework.command.api.commands.operator.delegate.settings;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.exception.lib.UnawaitedRawCommandException;
import ru.ancap.framework.command.api.commands.exception.lib.UnknownSubCommandException;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;

@ToString @EqualsAndHashCode
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandOperator unknown = dispatch -> {
        boolean raw = dispatch.command().isRaw();
        if (raw) throw new UnawaitedRawCommandException();
        else throw new UnknownSubCommandException(
            dispatch.command().nextPart().main(),
            dispatch.command().currentPartIndex()
        );
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