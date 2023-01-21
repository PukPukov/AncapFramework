package ru.ancap.framework.api.event.classic;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.CommandEvent;

/**
 * It's a CommandEvent that is fired when a command sender uses a command incorrectly
 */
@Getter
public class DescribedIncorrectArgumentsEvent extends CommandEvent {

    private final String description;

    public DescribedIncorrectArgumentsEvent(CommandSender sender, String description) {
        super(sender);
        this.description = description;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
