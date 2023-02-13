package ru.ancap.framework.command.api.commands.operator.delegator.settings;

import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;
import ru.ancap.framework.command.api.event.classic.UnknownCommandEvent;

/**
 * It's a delegator settings class that uses the `UnknownCommandEvent` as the default rule
 */
public class ClassicDelegatorSettings implements DelegatorSettings {

    private final CommandProvidePattern spareRule;
    private final CommandOperator unknown = dispatch -> Bukkit.getPluginManager().callEvent(
            new UnknownCommandEvent(dispatch.source().sender(), dispatch.command().nextArgument())
    );
    
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
