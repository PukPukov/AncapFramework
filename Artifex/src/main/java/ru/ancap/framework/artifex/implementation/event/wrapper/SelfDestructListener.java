package ru.ancap.framework.artifex.implementation.event.wrapper;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import ru.ancap.framework.api.event.events.additions.BlockNullifyEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

import java.util.List;
import java.util.stream.Collectors;

public class SelfDestructListener extends ArtifexListener {

    @EventHandler
    public void on(BlockFromToEvent event) {
        this.throwEvent(new WorldSelfDestructEvent(event, event.getBlock().getLocation(), List.of(event.getBlock().getLocation())));
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockBurnEvent event) {
        if (event.getIgnitingBlock() != null) this.throwEvent(new WorldSelfDestructEvent(
                event, 
                event.getIgnitingBlock().getLocation(), 
                List.of(event.getBlock().getLocation())
        ));
        this.throwEvent(new BlockNullifyEvent(event, List.of(event.getBlock()), false));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockPistonExtendEvent event) {
        this.piston(event, event.getBlocks());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockPistonRetractEvent event) {
        this.piston(event, List.of(event.getBlock()));
    }

    private void piston(BlockPistonEvent event, List<Block> moveds) {
        this.throwEvent(new WorldSelfDestructEvent(event, event.getBlock().getLocation(), moveds.stream()
                .map(Block::getLocation)
                .collect(Collectors.toList())
        ));
        this.throwEvent(new BlockNullifyEvent(event, moveds, true));
    }
    
}
