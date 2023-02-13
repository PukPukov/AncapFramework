package ru.ancap.framework.command.api.commands.operator.communicate;

import lombok.AllArgsConstructor;
import ru.ancap.communicate.Communicator;
import ru.ancap.communicate.message.CallableMessage;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

@AllArgsConstructor
public class Adviser implements CommandOperator {

    private CallableMessage message;

    @Override
    public void on(CommandDispatch dispatch) {
        new Communicator(dispatch.source().sender()).send(this.message);
    }
    
}
