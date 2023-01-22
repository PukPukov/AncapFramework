package ru.ancap.framework.api.command.commands.operator.finite;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.command.executor.CommandSpeaker;
import ru.ancap.framework.api.command.commands.operator.finite.pattern.CommandEventPattern;


@AllArgsConstructor
public class FiniteCommandTarget implements CommandOperator {

    private final CommandEventPattern patternalizer;
    @Delegate
    private final CommandSpeaker speaker;

    public FiniteCommandTarget(CommandEventPattern patternalizer) {
        this(patternalizer, new CommandSpeaker.Empty());
    }

    @Override
    public void on(CommandDispatch dispatch) {
        Bukkit.getPluginManager().callEvent(
                patternalizer.patternalize(dispatch.getSender(), dispatch.getCommand())
        );
    }
    
}
