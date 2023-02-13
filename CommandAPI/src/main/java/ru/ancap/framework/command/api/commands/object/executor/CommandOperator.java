package ru.ancap.framework.command.api.commands.object.executor;

import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

public interface CommandOperator extends CommandExecutor, CommandSpeaker {
    
    CommandOperator EMPTY = dispatch -> {};
    
    default void on(CommandWrite write) {
        
    }
    
}
