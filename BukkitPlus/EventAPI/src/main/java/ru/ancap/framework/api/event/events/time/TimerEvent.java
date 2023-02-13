package ru.ancap.framework.api.event.events.time;

import ru.ancap.framework.api.event.events.wrapper.AncapEvent;

public abstract class TimerEvent extends AncapEvent {

    public TimerEvent() {
        super(false);
    }


}
