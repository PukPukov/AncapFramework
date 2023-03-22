package ru.ancap.framework.api.event.events.time;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.api.event.events.wrapper.AncapEvent;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public abstract class TimerEvent extends AncapEvent {

    public TimerEvent() {
        super(false);
    }


}
