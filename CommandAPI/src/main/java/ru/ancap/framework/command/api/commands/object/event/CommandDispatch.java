package ru.ancap.framework.command.api.commands.object.event;

import lombok.AllArgsConstructor;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

@AllArgsConstructor
public class CommandDispatch {
    
    private final CommandSource source;
    private final LeveledCommand command;
    
    public CommandSource source() {
        return this.source;
    }
    
    public LeveledCommand command() {
        return this.command;
    }
    
}
