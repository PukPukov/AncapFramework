package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;

@ToString @EqualsAndHashCode
public class ArgumentsOperateRule implements OperateRule {

    private final String key;

    public ArgumentsOperateRule(@NotNull String key) {
        this.key = key;
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        return new ArgumentsOperatedCommand(command).hasArgument(this.key);
    }

    @AllArgsConstructor
    private static class ArgumentsOperatedCommand {
        
        private final LeveledCommand command;

        boolean hasArgument(String key) {
            try {
                return this.command.nextArgument().equalsIgnoreCase(key);
            } catch (NoNextArgumentException e) {
                return false;
            }
        }
        
    }
}
