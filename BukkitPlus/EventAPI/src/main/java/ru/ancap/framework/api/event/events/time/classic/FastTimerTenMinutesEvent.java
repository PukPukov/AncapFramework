package ru.ancap.framework.api.event.events.time.classic;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.events.time.TimerEvent;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class FastTimerTenMinutesEvent extends TimerEvent {

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}
