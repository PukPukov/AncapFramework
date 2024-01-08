package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.ArgumentsOperateRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString @EqualsAndHashCode
public class StringDelegatePattern implements DelegatePattern {

    private final String mainCommand;
    private final List<ArgumentsOperateRule> keys;

    protected StringDelegatePattern(String mainCommand, List<ArgumentsOperateRule> keys) {
        this.mainCommand = mainCommand;
        this.keys = keys;
    }

    public StringDelegatePattern(String main, String... aliases) {
        this(main, from(main, aliases));
    }

    private static List<ArgumentsOperateRule> from(String main, String[] aliases) {
        ArrayList<String> stringKeys = new ArrayList<>(List.of(aliases));
        stringKeys.add(main);
        ArrayList<ArgumentsOperateRule> list = new ArrayList<>();
        for (String string : stringKeys) {
            list.add(new ArgumentsOperateRule(string));
        }
        return list;
    }

    @Override
    public boolean isOperate(CSCommand command) {
        for (ArgumentsOperateRule key : this.keys) {
            if (key.isOperate(command)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> candidates() {
        return Collections.singletonList(mainCommand);
    }
}
