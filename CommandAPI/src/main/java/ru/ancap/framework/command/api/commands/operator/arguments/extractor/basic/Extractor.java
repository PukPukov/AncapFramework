package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

import java.util.function.Function;
public class Extractor<T> extends PrimitiveExtractor<T> {
    
    private final Function<String, T> function;
    
    public Extractor(Class<T> type, Function<String, T> function) {
        super(type);
        this.function = function;
    }

    @Override
    protected T provide(String string) throws Throwable {
        return this.function.apply(string);
    }
    
}
