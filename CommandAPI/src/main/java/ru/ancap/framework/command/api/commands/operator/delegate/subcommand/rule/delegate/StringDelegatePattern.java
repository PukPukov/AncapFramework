package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class StringDelegatePattern implements DelegatePattern {

    private final String mainCommand;
    private final List<String> keys;

    public StringDelegatePattern(String main, String... aliases) {
        this(main, from(main, aliases));
    }

    private static List<String> from(String main, String[] aliases) {
        ArrayList<String> stringKeys = new ArrayList<>(List.of(aliases));
        stringKeys.add(main);
        return stringKeys;
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        if (command.isRaw()) return false;
        for (String key : this.keys) {
            if (key.equalsIgnoreCase(command.nextPart().main())) return true;
        }
        return false;
    }

    @Override
    public List<String> candidates() {
        return Collections.singletonList(this.mainCommand);
    }
}