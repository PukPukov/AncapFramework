package ru.ancap.framework.api.event.classic;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.CommandEvent;

/**
 * It's an event that is called when a user enters an incorrect argument
 */
@Getter
public class IncorrectArgumentEvent extends CommandEvent {

    private String argument;

    public IncorrectArgumentEvent(CommandSender sender, String argument) {
        super(sender);
        this.argument = argument;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
