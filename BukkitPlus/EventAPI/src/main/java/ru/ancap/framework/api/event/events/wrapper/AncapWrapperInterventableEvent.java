package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true) @Getter
public abstract class AncapWrapperInterventableEvent extends AncapEvent implements Cancellable, Consumable {
    
    @Delegate
    private final @NotNull Cancellable bukkit;
    
    private boolean consumed = false;

    public AncapWrapperInterventableEvent(@NonNull Cancellable event) {
        super(false);
        this.bukkit = event;
    }
    
    @Override
    public void consume() {
        this.consumed = true;
    }
    
}