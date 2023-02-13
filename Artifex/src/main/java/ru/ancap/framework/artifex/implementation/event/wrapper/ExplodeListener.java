package ru.ancap.framework.artifex.implementation.event.wrapper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import ru.ancap.framework.api.event.events.additions.BlockNullifyEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

import java.util.List;
import java.util.stream.Collectors;

public class ExplodeListener extends ArtifexListener {

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockExplodeEvent e) {
        List<Location> exploded = e.blockList().stream()
                .map(Block::getLocation)
                .collect(Collectors.toList());
        this.throwEvent(new WorldSelfDestructEvent(e, e.getBlock().getLocation(), exploded));
        this.throwEvent(new BlockNullifyEvent(e, e.blockList(), false));
    }
    
}
