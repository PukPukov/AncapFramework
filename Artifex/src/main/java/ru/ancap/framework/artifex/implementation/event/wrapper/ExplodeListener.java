package ru.ancap.framework.artifex.implementation.event.wrapper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import ru.ancap.framework.api.event.events.additions.BlockNullifyEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

import java.util.List;

public class ExplodeListener extends ArtifexListener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockExplodeEvent event) {
        this.operate(event, event.getBlock().getLocation(), event.blockList());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(EntityExplodeEvent event) {
        this.operate(event, event.getLocation(), event.blockList());
    }
    
    private void operate(Cancellable event, Location active, List<Block> passive) {
        List<Location> exploded = passive.stream()
            .map(Block::getLocation)
            .toList();
        this.throwEvent(new WorldSelfDestructEvent(event, active, exploded));
        this.throwEvent(new BlockNullifyEvent(event, passive, false));
    }
    
}
