package ru.ancap.framework.api.event.events.wrapper;

import org.bukkit.event.Event;

public abstract class AncapEvent extends Event {

    public AncapEvent(boolean isAsync) {
        super(isAsync);
    }


}
