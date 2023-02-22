package ru.ancap.framework.command.api.event.classic;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;

public class NotEnoughPermissionsEvent extends CommandEvent {

    private final String lackedPermission;

    public NotEnoughPermissionsEvent(CommandSender sender, String lackedPermission) {
        super(sender);
        this.lackedPermission = lackedPermission;
    }
    
    public String lackedPermission() {
        return this.lackedPermission;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
