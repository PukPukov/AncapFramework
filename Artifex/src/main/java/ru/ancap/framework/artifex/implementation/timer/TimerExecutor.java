package ru.ancap.framework.artifex.implementation.timer;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NonBlocking;
import ru.ancap.framework.api.event.events.time.classic.FastTimerTenMinutesEvent;
import ru.ancap.framework.api.event.events.time.classic.FastTimerTenSecondEvent;

import java.util.function.Supplier;

@AllArgsConstructor
public class TimerExecutor implements Runnable {
    
    private final JavaPlugin plugin;
    
    @Override
    public void run() {
        this.repeat(FastTimerTenSecondEvent::new, 10);
        this.repeat(FastTimerTenMinutesEvent::new, 600);
    }

    @NonBlocking
    private void repeat(Supplier<Event> toCall, int period) {
        Bukkit.getScheduler().runTaskTimer(
            this.plugin,
            () -> Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getPluginManager().callEvent(toCall.get())),
            0,
            period
        );
    }
    
}
