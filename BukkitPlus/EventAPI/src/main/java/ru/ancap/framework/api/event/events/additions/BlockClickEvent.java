package ru.ancap.framework.api.event.events.additions;

import lombok.experimental.Delegate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BlockClickEvent extends Event implements Cancellable {

    @Delegate
    private final @NotNull Cancellable event;
    private final @NotNull Player clicker;
    private final @NotNull Block block;

    public BlockClickEvent(@NotNull Cancellable event, @NotNull Player player, @NotNull Block block) {
        this.event = event;
        this.block = block;
        this.clicker = player;
    }

    public Cancellable bukkitEvent() {
        return this.event;
    }
    
    public Player clicker() {
        return this.clicker;
    }

    public Block block() {
        return this.block;
    }
    
    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}
