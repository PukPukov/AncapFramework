package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;

@ToString @EqualsAndHashCode
public class ArgumentsOperateRule implements OperateRule {

    private final String key;

    public ArgumentsOperateRule(@NonNull String key) {
        this.key = key;
    }

    @Override
    public boolean isOperate(CSCommand command) {
        return new ArgumentsOperatedCommand(command).hasArgument(this.key);
    }

    private record ArgumentsOperatedCommand(CSCommand command) {
        
        boolean hasArgument(String key) {
            try {
                return this.command.consumeArgument().equalsIgnoreCase(key);
            } catch (NoNextArgumentException e) {
                return false;
            }
        }
    
    }
    
}
