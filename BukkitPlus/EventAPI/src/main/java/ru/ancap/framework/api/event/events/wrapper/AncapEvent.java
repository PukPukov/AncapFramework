package ru.ancap.framework.api.event.events.wrapper;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.event.Event;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public abstract class AncapEvent extends Event {

    public AncapEvent(boolean isAsync) {
        super(isAsync);
    }

}
