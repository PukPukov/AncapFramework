package ru.ancap.framework.artifex.implementation.timer;

import org.bukkit.Bukkit;
import ru.ancap.framework.api.event.events.time.classic.SafeTimerOneDayEvent;

public class EveryDayTask implements Runnable {
    
    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new SafeTimerOneDayEvent());
    }
    
}
