package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.exception.exception.UnpermittedActionException;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Exclusive extends CommandTarget {

    public Exclusive(Pass pass, CommandOperator delegate) {
        super(new CommandOperator() {
            
            @Override
            public void on(CommandDispatch dispatch) {
                if (!pass.allows(dispatch.source().sender())) {
                    throw new UnpermittedActionException(null, null); // TODO
                }
                delegate.on(dispatch);
            }
            
            @Override
            public void on(CommandWrite write) {
                if (!pass.allows(write.source().sender())) return;
                delegate.on(write);
            }
            
        });
    }
    
}