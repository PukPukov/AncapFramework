package ru.ancap.framework.command.api.commands.object.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

@AllArgsConstructor
@ToString @EqualsAndHashCode
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
