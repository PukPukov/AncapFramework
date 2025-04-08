package ru.ancap.framework.command.api.commands.operator.communicate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Advice implements CommandOperator {

    private CallableText message;

    @Override
    public void on(CommandDispatch dispatch) {
        Communicator.of(dispatch.source().sender()).message(this.message);
    }
    
}