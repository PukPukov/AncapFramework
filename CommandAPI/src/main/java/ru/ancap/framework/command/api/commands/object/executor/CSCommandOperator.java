package ru.ancap.framework.command.api.commands.object.executor;

import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

public interface CSCommandOperator extends CommandExecutor, CommandSpeaker {
    
    CSCommandOperator EMPTY = dispatch -> {};
    
    default void on(CommandWrite write) {
        
    }
    
}
