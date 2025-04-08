package ru.ancap.framework.command.api.commands.object.executor;

import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

public interface CommandExceptionOperator<T extends Throwable> {
    
    void on(T exception, CommandSource source, LeveledCommand leveledCommand);
    
    default void on(T exception, CommandDispatch dispatch) {
        this.on(exception, dispatch.source(), dispatch.command());
    }
    
    default void on(T exception, CommandWrite write) {
        this.on(exception, write.source(), write.line());
    }
    
}