package ru.ancap.framework.command.api.commands.operator.exclusive;

import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.event.classic.NotEnoughPermissionsEvent;

public class Exclusive extends CommandTarget {

    public Exclusive(Pass pass, CommandOperator delegate) {
        super(new CommandOperator() {
            
            @Override
            public void on(CommandDispatch dispatch) {
                if (!pass.allows(dispatch.source().sender())) {
                    Bukkit.getPluginManager().callEvent(new NotEnoughPermissionsEvent(dispatch.source().sender()));
                    return;
                }
                delegate.on(dispatch);
            }
            
            @Override
            public void on(CommandWrite write) {
                if (!pass.allows(write.speaker().source().sender())) return;
                delegate.on(write);
            }
            
        });
    }
    
}
