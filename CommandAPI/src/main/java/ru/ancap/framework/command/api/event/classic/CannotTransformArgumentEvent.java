package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;

import java.util.List;

public class CannotTransformArgumentEvent extends CommandEvent {
    
    private final List<String> argument;
    private final Class<?> type;
    
    public CannotTransformArgumentEvent(CommandSender sender, List<String> argument, Class<?> type) {
        super(sender);
        this.argument = argument;
        this.type = type;
    }
    
    public List<String> argument() {
        return this.argument;
    }
    
    public Class<?> type() {
        return this.type;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
    
}
