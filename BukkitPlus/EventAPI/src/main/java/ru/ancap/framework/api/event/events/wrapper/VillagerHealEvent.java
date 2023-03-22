package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Calling, when some player tries to heal villager
 */

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class VillagerHealEvent extends AncapWrapperPlayerEvent implements Cancellable {

    private final @NotNull Villager villager;

    public VillagerHealEvent(@NotNull Cancellable event, @NotNull Player player, @NotNull Villager villager) {
        super(event, player);
        this.villager = villager;
    }
    
    public Villager villager() {
        return this.villager;
    }

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}

}
