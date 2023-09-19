package ru.ancap.framework.api.event.events.wrapper;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Calling, when some player interacts with the world
 */
public class WorldInteractEvent extends AncapWrapperPlayerEvent implements Cancellable {

    private final @NotNull List<Location> locations;

    public WorldInteractEvent(@NonNull Cancellable event, @NonNull Player player, @NonNull List<Location> locations) {
        super(event, player);
        this.locations = locations;
    }
    
    public List<Location> locations() {
        return this.locations;
    }

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}
