package ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.delegate.operate;

import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.exception.NoNextArgumentException;

public class ArgumentsOperateRule implements OperateRule {

    private final String key;

    public ArgumentsOperateRule(@NotNull String key) {
        this.key = key;
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        return new ArgumentsOperatedCommand(command).hasArgument(key);
    }

    private record ArgumentsOperatedCommand(LeveledCommand command) {

        boolean hasArgument(String key) {
            try {
                return command.nextArgument().equalsIgnoreCase(key);
            } catch (NoNextArgumentException e) {
                return false;
            }
        }
    }
}
