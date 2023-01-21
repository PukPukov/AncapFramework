package ru.ancap.framework.api.command.commands.command.executor;

import ru.ancap.framework.api.command.commands.command.event.CommandWrite;

public interface CommandOperator extends CommandExecutor, CommandSpeaker {
    
    CommandOperator EMPTY = dispatch -> {};
    
    default void on(CommandWrite write) {
        
    }
    
}
