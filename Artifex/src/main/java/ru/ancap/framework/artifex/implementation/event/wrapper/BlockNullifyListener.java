package ru.ancap.framework.artifex.implementation.event.wrapper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import ru.ancap.framework.api.event.events.additions.BlockNullifyEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

import java.util.List;

public class BlockNullifyListener extends ArtifexListener {
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockFadeEvent event) {
        this.throwEvent(new BlockNullifyEvent(event, List.of(event.getBlock()), false));
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockBurnEvent event) {
        this.throwEvent(new BlockNullifyEvent(event, List.of(event.getBlock()), false));
    }
    
}
