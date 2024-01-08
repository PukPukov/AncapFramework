package ru.ancap.framework.command.api.exception.reactor;

public interface ExceptionReactor {

    void accept(Throwable exception);
    void registerHandler(Class<?> exceptionClass, ExceptionHandler exceptionHandler);

    interface ExceptionHandler {
        void handle(Throwable exception);
    }

}
