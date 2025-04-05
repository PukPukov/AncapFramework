package ru.ancap.framework.plugin.api.commands.exception;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.plugin.util.CommandErrorMessage;

@RequiredArgsConstructor
public class MessageExceptionOperator<T extends Throwable> implements CommandExceptionOperator<T> {
    
    private final CallableMessage message;
    
    @Override
    public void on(T throwable, CommandSource source, LeveledCommand command) {
        CommandErrorMessage.send(source.sender(), this.message);
    }
    
}