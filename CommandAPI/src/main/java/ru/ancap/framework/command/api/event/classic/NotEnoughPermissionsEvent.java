package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;

public class NotEnoughPermissionsEvent extends CommandEvent {

    private final CallableMessage lackedPermission;
    
    public NotEnoughPermissionsEvent(CommandSender sender, String lackedPermission) {
        super(sender);
        this.lackedPermission = new Message(lackedPermission);
    }

    public NotEnoughPermissionsEvent(CommandSender sender, CallableMessage lackedPermission) {
        super(sender);
        this.lackedPermission = lackedPermission;
    }
    
    public CallableMessage lackedPermission() {
        return this.lackedPermission;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
