package ru.ancap.framework.command.api.event;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
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
