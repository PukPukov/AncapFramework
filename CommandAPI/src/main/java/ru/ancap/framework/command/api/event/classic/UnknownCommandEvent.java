package ru.ancap.framework.command.api.event.classic;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;

public class UnknownCommandEvent extends CommandEvent {

    private final String unknownValue;

    public UnknownCommandEvent(@NonNull CommandSender sender, @NonNull String unknownValue) {
        super(sender);
        this.unknownValue = unknownValue;
    }
    
    public String unknownValue() {
        return this.unknownValue;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
