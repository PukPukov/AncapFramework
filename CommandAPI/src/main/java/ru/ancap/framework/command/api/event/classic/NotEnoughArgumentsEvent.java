package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;

/**
 * It's an event that is fired when a command sender doesn't provide enough arguments
 */
public class NotEnoughArgumentsEvent extends CommandEvent {

    private final int argumentsLack;

    public NotEnoughArgumentsEvent(CommandSender sender, int argumentsLack) {
        super(sender);
        this.argumentsLack = argumentsLack;
    }
    
    public int argumentsLack() {
        return this.argumentsLack;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
