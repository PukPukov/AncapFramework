package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

public class DoubleExtractor extends PrimitiveExtractor<Double> {
    public DoubleExtractor() {
        super(Double.class);
    }

    @Override
    protected Double provide(String string) throws Throwable {
        return Double.parseDouble(string);
    }
}
