package AncapLibrary.Timer.ClassicTimerEvents;

import AncapLibrary.Timer.AncapTimerEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FastTimerTenMinutesEvent extends AncapTimerEvent {

    public static final HandlerList handlers = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
