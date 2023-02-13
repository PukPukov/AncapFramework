package ru.ancap.framework.api.event.events.wrapper;

import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public abstract class AncapWrapperCancellableEvent extends AncapEvent implements Cancellable {

    @Delegate
    private final Cancellable bukkitEvent;

    public AncapWrapperCancellableEvent(Cancellable event) {
        super(false);
        this.bukkitEvent = event;
    }

    @NotNull
    public Cancellable getBukkitEvent() {
        return this.bukkitEvent;
    }
    
}
