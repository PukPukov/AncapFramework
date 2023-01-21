package ru.ancap.framework.api.command.commands.transformer.basic;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.api.command.commands.transformer.TransformationException;
import ru.ancap.framework.api.command.commands.transformer.Transformer;

@RequiredArgsConstructor
public abstract class AbstractTransformer<T> implements Transformer<T> {
    
    private final Class<T> type;
    
    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public T transform(String string) throws TransformationException {
        try {
            return this.provide(string);
        } catch (Throwable throwable) {
            throw new TransformationException(string, type);
        }
    }
    
    protected abstract T provide(String string) throws Throwable;
    
}
