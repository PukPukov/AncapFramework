package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

@ToString @EqualsAndHashCode
public class ArgumentsOperateRule implements OperateRule {

    private final String key;

    public ArgumentsOperateRule(@NonNull String key) {
        this.key = key;
    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        return new ArgumentsOperatedCommand(command).hasArgument(this.key);
    }

    private record ArgumentsOperatedCommand(LeveledCommand command) {
        
        boolean hasArgument(String key) {
            if (this.command.isRaw()) return false;
            return this.command.nextPart().equalsIgnoreCase(key);
        }
    
    }
    
}