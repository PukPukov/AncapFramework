package ru.ancap.framework.plugin.event.timer;

import org.bukkit.event.EventHandler;
import ru.ancap.framework.api.event.events.time.classic.FastTimerTenMinutesEvent;
import ru.ancap.framework.api.event.events.time.classic.FastTimerTenSecondEvent;
import ru.ancap.framework.api.event.events.time.heartbeat.AncapHeartbeatEvent;
import ru.ancap.framework.plugin.event.listeners.ArtifexListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ArtifexTimerEventListener extends ArtifexListener {

    private int tenSecondsCount = 0;
    private int tenMinutesCount = 0;

    private int day;

    private final Calendar calendar = new GregorianCalendar();

    @EventHandler
    public void onHeartbeat(AncapHeartbeatEvent e) {
        if (this.tenSecondsCount>10) {
            this.throwEvent(new FastTimerTenSecondEvent());
            this.tenSecondsCount = 0;
        }
        if (this.tenMinutesCount>600) {
            this.throwEvent(new FastTimerTenMinutesEvent());
            this.tenMinutesCount = 0;
        }
        if (this.getDay() != this.day) {
            // this.throwEvent(new SafeTimerOneDayEvent());
            this.day = this.getDay();
        }
        this.tickTimers();
    }

    private void tickTimers() {
        this.tenSecondsCount = this.tenSecondsCount+1;
        this.tenMinutesCount = this.tenMinutesCount+1;
    }

    private int getDay() {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
