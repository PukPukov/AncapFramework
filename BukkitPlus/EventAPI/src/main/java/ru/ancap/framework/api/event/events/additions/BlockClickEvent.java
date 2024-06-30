package ru.ancap.framework.api.event.events.additions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true) @Getter
public class BlockClickEvent extends Event implements Cancellable {

    @Delegate
    private final @NotNull Cancellable bukkit;
    private final @NotNull Player clicker;
    private final @NotNull Block block;

    public BlockClickEvent(@NonNull Cancellable bukkit, @NonNull Player player, @NonNull Block block) {
        this.bukkit = bukkit;
        this.block = block;
        this.clicker = player;
    }
    
    /**
     * @deprecated use bukkit()
     */
    @Deprecated
    public Cancellable bukkitEvent() {
        return this.bukkit;
    }
    
    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}