package ru.ancap.framework.command.api.commands.exception;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.message.CallableText;

@RequiredArgsConstructor
public class MessageExceptionOperator<T extends Throwable> implements CommandExceptionOperator<T> {
    
    private final CallableText message;
    
    @Override
    public void on(T throwable, CommandSource source, LeveledCommand command) {
        CommandErrorText.send(source.sender(), this.message);
    }
    
}