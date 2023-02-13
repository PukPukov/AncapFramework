package ru.ancap.framework.command.api.event;

import org.bukkit.command.CommandSender;

public abstract class CommandEvent extends OperableEvent {

    private final CommandSender sender;

    public CommandEvent(CommandSender sender) {
        super(true);
        this.sender = sender;
    }
    
    public CommandSender sender() {
        return this.sender;
    }
    
}
