package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public abstract class AncapWrapperInterventableEvent extends AncapEvent implements Cancellable, Consumable {
    
    @Delegate
    private final Cancellable bukkitEvent;
    
    private boolean consumed = false;

    public AncapWrapperInterventableEvent(Cancellable event) {
        super(false);
        this.bukkitEvent = event;
    }
    
    public @NotNull Cancellable bukkit() {
        return this.bukkitEvent;
    }
    
    @Override
    public boolean consumed() {
        return this.consumed;
    }
    
    @Override
    public void consume() {
        this.consumed = true;
    }
    
}
