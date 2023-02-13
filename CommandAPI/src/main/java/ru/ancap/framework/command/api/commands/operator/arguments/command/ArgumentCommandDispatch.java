package ru.ancap.framework.command.api.commands.operator.arguments.command;

import lombok.AllArgsConstructor;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsBundle;

@AllArgsConstructor
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
