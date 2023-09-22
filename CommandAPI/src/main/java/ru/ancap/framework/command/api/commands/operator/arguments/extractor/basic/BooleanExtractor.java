package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

public class BooleanExtractor extends PrimitiveExtractor<Boolean> {
    
    public BooleanExtractor() {
        super(Boolean.class);
    }

    @Override
    protected Boolean provide(String string) {
        return Boolean.valueOf(string);
    }
    
}
