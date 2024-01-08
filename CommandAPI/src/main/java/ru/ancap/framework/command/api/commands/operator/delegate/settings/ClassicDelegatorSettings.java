package ru.ancap.framework.command.api.commands.operator.delegate.settings;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;
import ru.ancap.framework.command.api.event.classic.UnknownCommandEvent;

@ToString @EqualsAndHashCode
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandProvidePattern spareRule;
    private final CSCommandOperator unknown = dispatch -> Bukkit.getPluginManager().callEvent(
            new UnknownCommandEvent(dispatch.source().sender(), dispatch.command().consumeArgument())
    );
    
    public CommandProvidePattern spareRule() {
        return this.spareRule;
    }
    
    public CSCommandOperator unknown() {
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
