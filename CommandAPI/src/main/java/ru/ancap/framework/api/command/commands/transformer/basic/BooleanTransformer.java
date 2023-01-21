package ru.ancap.framework.api.command.commands.transformer.basic;

public class BooleanTransformer extends AbstractTransformer<Boolean> {
    public BooleanTransformer() {
        super(Boolean.class);
    }

    @Override
    protected Boolean provide(String string) {
        return Boolean.valueOf(string);
    }
}
