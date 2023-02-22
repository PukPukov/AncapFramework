package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;
import ru.ancap.framework.communicate.message.CallableMessage;

public class UnexsecutableCommandEvent extends CommandEvent {

    private final CallableMessage description;

    public UnexsecutableCommandEvent(@NotNull CommandSender sender, @NotNull CallableMessage description) {
        super(sender);
        this.description = description;
    }

    public CallableMessage description() {
        return this.description;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
    
}
