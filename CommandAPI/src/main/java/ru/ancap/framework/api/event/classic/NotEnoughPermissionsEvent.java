package ru.ancap.framework.api.event.classic;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.CommandEvent;

/**
 * It's an event that is called when a user doesn't have enough permissions to execute a command
 */
@Getter
public class NotEnoughPermissionsEvent extends CommandEvent {

    private final String lackedPermission;

    public NotEnoughPermissionsEvent(CommandSender sender, String lackedPermission) {
        super(sender);
        this.lackedPermission = lackedPermission;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}
