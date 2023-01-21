package ru.ancap.framework.api.command.commands.transformer.basic;

public class DoubleTransformer extends AbstractTransformer<Double> {
    public DoubleTransformer() {
        super(Double.class);
    }

    @Override
    protected Double provide(String string) throws Throwable {
        return Double.parseDouble(string);
    }
}
