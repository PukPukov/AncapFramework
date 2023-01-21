package ru.ancap.framework.api.command.commands.transformer.basic;

public class Self extends AbstractTransformer<String> {
    
    public Self() {
        super(String.class);
    }
    
    @Override
    protected String provide(String string) {
        return string;
    }
}
