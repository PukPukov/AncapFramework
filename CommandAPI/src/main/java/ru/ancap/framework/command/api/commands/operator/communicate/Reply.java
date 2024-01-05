package ru.ancap.framework.command.api.commands.operator.communicate;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class Reply implements CommandOperator {
    
    private final Function<CommandSource, CallableMessage> messageFunction;
    
    public Reply(Supplier<CallableMessage> messageSupplier) {
        this.messageFunction = (ignored) -> messageSupplier.get();
    }

    @Override
    public void on(CommandDispatch dispatch) {
        Communicator.of(dispatch.source().sender()).message(this.messageFunction.apply(dispatch.source()));
    }
    
}
