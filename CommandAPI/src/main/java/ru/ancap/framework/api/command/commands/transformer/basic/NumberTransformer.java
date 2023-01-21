package ru.ancap.framework.api.command.commands.transformer.basic;

public class NumberTransformer extends AbstractTransformer<Long> {

    public NumberTransformer() {
        super(Long.class);
    }

    @Override
    protected Long provide(String string) {
        return Long.valueOf(string);
    }
}
