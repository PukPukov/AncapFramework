package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.CSCommandTarget;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.event.classic.NotEnoughPermissionsEvent;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Exclusive extends CSCommandTarget {

    public Exclusive(Pass pass, CSCommandOperator delegate) {
        super(new CSCommandOperator() {
            
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
