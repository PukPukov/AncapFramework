package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

public class Self extends PrimitiveExtractor<String> {
    
    public Self() {
        super(String.class);
    }
    
    @Override
    protected String provide(String string) {
        return string;
    }
}
