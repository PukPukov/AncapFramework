package ru.ancap.framework.command.api.commands.operator.communicate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Advice implements CSCommandOperator {

    private CallableMessage message;

    @Override
    public void on(CommandDispatch dispatch) {
        Communicator.of(dispatch.source().sender()).message(this.message);
    }
    
}
