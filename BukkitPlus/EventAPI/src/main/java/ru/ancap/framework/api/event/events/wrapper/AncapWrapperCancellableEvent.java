package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
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
