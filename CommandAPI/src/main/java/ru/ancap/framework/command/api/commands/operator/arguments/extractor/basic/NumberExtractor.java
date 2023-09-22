package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

public class NumberExtractor extends PrimitiveExtractor<Long> {

    public NumberExtractor() {
        super(Long.class);
    }

    @Override
    protected Long provide(String string) {
        return Long.valueOf(string);
    }
    
}
