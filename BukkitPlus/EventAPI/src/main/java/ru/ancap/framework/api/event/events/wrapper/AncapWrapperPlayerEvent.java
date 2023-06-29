package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public abstract class AncapWrapperPlayerEvent extends AncapWrapperInterventableEvent {

    private final @NotNull Player player;
    
    public Player player() {
        return this.player;
    }

    public AncapWrapperPlayerEvent(Cancellable event, @NotNull Player player) {
        super(event);
        this.player = player;
    }
    
}
