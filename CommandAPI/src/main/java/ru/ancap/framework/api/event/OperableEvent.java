package ru.ancap.framework.api.event;

import ru.ancap.framework.api.event.events.wrapper.AncapEvent;

public abstract class OperableEvent extends AncapEvent {

    private boolean operated;

    public OperableEvent(boolean isAsync) {
        super(isAsync);
    }

    /**
     * @return Returns the fact the event is not alredy operated and set's it as operated in the same time.
     */
    public boolean operate() {
        boolean old = operated;
        this.operated = true;
        return !old;
    }

}
