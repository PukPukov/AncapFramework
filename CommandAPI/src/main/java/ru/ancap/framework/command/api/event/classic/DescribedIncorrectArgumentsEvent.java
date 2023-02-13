package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;

/**
 * It's a CommandEvent that is fired when a command sender uses a command incorrectly
 */
public class DescribedIncorrectArgumentsEvent extends CommandEvent {

    private final String description;

    public DescribedIncorrectArgumentsEvent(CommandSender sender, String description) {
        super(sender);
        this.description = description;
    }
    
    public String description() {
        return this.description;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
