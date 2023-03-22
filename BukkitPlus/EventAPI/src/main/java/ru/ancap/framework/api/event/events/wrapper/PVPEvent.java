package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Calling, when some player attacks other player(s).
 */

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class PVPEvent extends AncapWrapperCancellableEvent implements Cancellable {

    private final @NotNull Player attacker;
    private final @NotNull List<Player> attacked;

    public PVPEvent(@NotNull Cancellable event, @NotNull Player attacker, @NotNull List<Player> attacked) {
        super(event);
        this.attacker = attacker;
        this.attacked = attacked;
    }
    
    public Player attacker() {
        return this.attacker;
    }
    
    public List<Player> attacked() {
        return this.attacked;
    }

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}
