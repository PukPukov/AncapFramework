package ru.ancap.framework.command.api.commands.object.executor;

import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

public interface CommandExceptionOperator<T extends Throwable> {
    
    void on(T throwable, CommandSource source, LeveledCommand leveledCommand);
    
    default void on(T throwable, CommandDispatch dispatch) {
        this.on(throwable, dispatch.source(), dispatch.command());
    }
    
    default void on(T throwable, CommandWrite write) {
        this.on(throwable, write.speaker().source(), write.line());
    }
    
}