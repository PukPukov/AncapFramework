package ru.ancap.framework.api.command.commands.transformer;

public interface Transformer<T> {

    Class<T> type();
    T transform(String string) throws TransformationException;

}
