package ru.ancap.framework.command.api.exception.reactor;

import ru.ancap.framework.command.api.exception.exceptions.UnhandledException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseCommandExceptionReactor implements ExceptionReactor {

    private final Map<Class<?>, ExceptionHandler> registry = new ConcurrentHashMap<>();

    @Override
    public void accept(Throwable throwable) {
        this.accept(throwable, true);
    }
    
    private void accept(Throwable throwable, boolean fallback) {
        ExceptionHandler handler = this.registry.get(throwable.getClass());
        if (handler == null) {
            UnhandledException exception = new UnhandledException(throwable);
            if (fallback) { this.accept(exception, false); return; }
            else throw exception;
        }
        handler.handle(throwable);
    }

    @Override
    public void registerHandler(Class<?> exceptionClass, ExceptionHandler exceptionHandler) {
        this.registry.put(exceptionClass, exceptionHandler);
    }

}
