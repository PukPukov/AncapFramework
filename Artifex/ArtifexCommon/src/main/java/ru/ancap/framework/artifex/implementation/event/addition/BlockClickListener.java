package ru.ancap.framework.artifex.implementation.event.addition;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.ancap.framework.api.event.events.additions.BlockClickEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

public class BlockClickListener extends ArtifexListener {

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        this.throwEvent(new BlockClickEvent(event, event.getPlayer(), event.getClickedBlock()));
    }
    
}
