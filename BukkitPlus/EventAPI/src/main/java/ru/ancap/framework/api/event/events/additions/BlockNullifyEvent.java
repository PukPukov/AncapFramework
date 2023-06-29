package ru.ancap.framework.api.event.events.additions;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.events.wrapper.AncapWrapperInterventableEvent;

import java.util.List;

/**
 * Called, when blocks disappears.
 */
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class BlockNullifyEvent extends AncapWrapperInterventableEvent implements Cancellable {
    
    private final @NotNull List<Block> disappeared;

    /**
     * Indicates, that blocks is not fully disappeared, but moved.
     */
    private final boolean isMoved;

    public BlockNullifyEvent(@NotNull Cancellable event, @NotNull List<Block> disappeared, boolean moved) {
        super(event);
        this.disappeared = disappeared;
        this.isMoved = moved;
    }
    
    public List<Block> disappeared() {
        return this.disappeared;
    }
    
    public boolean moved() {
        return this.isMoved;
    }

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}

}
