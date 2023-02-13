package ru.ancap.framework.api.event.events.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class AncapWrapperPlayerEvent extends AncapWrapperCancellableEvent {

    private final @NotNull Player player;
    
    public Player player() {
        return this.player;
    }

    public AncapWrapperPlayerEvent(Cancellable event, @NotNull Player player) {
        super(event);
        this.player = player;
    }
    
}
