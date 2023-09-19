package ru.ancap.framework.command.api.event.classic;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;

public class NotEnoughPermissionsEvent extends CommandEvent {
    
    public NotEnoughPermissionsEvent(@NonNull CommandSender sender) {
        super(sender);
    }
    
    @Deprecated
    public NotEnoughPermissionsEvent(@NonNull CommandSender sender, String lackedPermission) {
        super(sender);
    }

    @Deprecated
    public NotEnoughPermissionsEvent(@NonNull CommandSender sender, CallableMessage lackedPermission) {
        super(sender);
    }
    
    @Deprecated
    public CallableMessage lackedPermission() {
        return new Message("Deprecated");
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
