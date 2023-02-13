package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

public class CharacterExtractor extends PrimitiveExtractor<Character> {
    public CharacterExtractor() {
        super(Character.class);
    }

    @Override
    protected Character provide(String string) throws Throwable {
        char[] chars = string.toCharArray();
        if (chars.length != 1) throw new IllegalStateException();
        return chars[0];
    }
}
