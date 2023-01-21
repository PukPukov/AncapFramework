package ru.ancap.framework.api.command.commands.transformer.basic;

public class CharacterTransformer extends AbstractTransformer<Character> {
    public CharacterTransformer() {
        super(Character.class);
    }

    @Override
    protected Character provide(String string) throws Throwable {
        char[] chars = string.toCharArray();
        if (chars.length != 1) throw new IllegalStateException();
        return chars[0];
    }
}
