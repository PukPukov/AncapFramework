package ru.ancap.framework.command.api.commands.operator.arguments.command;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsBundle;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class ArgumentCommandDispatch {
    
    private final CommandSource source;
    
    private final ArgumentsBundle arguments;
    
    public CommandSource source() {
        return this.source;
    }
    
    public ArgumentsBundle arguments() {
        return this.arguments;
    }
    
}
