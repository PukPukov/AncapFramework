package ru.ancap.framework.plugin.api.commands.exception;

import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;

public interface CommandExceptionCenter {
    
    <T extends Throwable> void register(Class<T> exception, CommandExceptionOperator<T> operator);
    <T extends Throwable> void unregister(Class<T> handledException);
    
}