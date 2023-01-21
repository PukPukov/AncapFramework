package ru.ancap.framework.api.event.classic;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.CommandEvent;

@Getter
public class CannotTransformArgumentEvent extends CommandEvent {
    
    private final String argument;
    private final Class<?> type;
    
    public CannotTransformArgumentEvent(CommandSender sender, String argument, Class<?> type) {
        super(sender);
        this.argument = argument;
        this.type = type;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
