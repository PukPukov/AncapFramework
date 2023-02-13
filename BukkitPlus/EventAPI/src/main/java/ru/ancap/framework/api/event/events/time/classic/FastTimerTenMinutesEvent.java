package ru.ancap.framework.api.event.events.time.classic;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.events.time.TimerEvent;

public class FastTimerTenMinutesEvent extends TimerEvent {

    public static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {return handlers;}
    @NotNull @Override public HandlerList getHandlers() {return handlers;}
    
}
