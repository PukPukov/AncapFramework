package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Calling, when world trying to destruct itself. May be called due to the influence 
 * of some player, when it is impossible to establish which player's this influence.
 * Don't try to find a player based on a destructor's location, it doesn't work that way. 
 * <p> 
 * For example, it called, when piston extracts or entity attacks other entity.
 */

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class WorldSelfDestructEvent extends AncapWrapperCancellableEvent implements Cancellable {
    
    private final @NotNull Location destructorPosition;
    private final @NotNull List<Location> destructionPositions;

    public WorldSelfDestructEvent(@NotNull Cancellable event, @NotNull Location destructorPosition, @NotNull List<Location> destructionPositions) {
        super(event);
        this.destructionPositions = destructionPositions;
        this.destructorPosition = destructorPosition;
    }
    
    public Location active() {
        return this.destructorPosition;
    }
    
    public List<Location> passive() {
        return this.destructionPositions;
    }

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}

}
