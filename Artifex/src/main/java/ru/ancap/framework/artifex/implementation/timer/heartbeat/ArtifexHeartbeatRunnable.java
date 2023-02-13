package ru.ancap.framework.artifex.implementation.timer.heartbeat;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ancap.framework.api.event.events.time.heartbeat.AncapHeartbeatEvent;

public class ArtifexHeartbeatRunnable extends BukkitRunnable {

    @Override
    public void run() {
        AncapHeartbeatEvent event = new AncapHeartbeatEvent();
        Bukkit.getPluginManager().callEvent(event);
    }
}
