package ru.ancap.framework.plugin.event.listeners.api.wrapper;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.plugin.PluginManager;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.plugin.event.listeners.ArtifexListener;

public class SelfDestructListener extends ArtifexListener {

    public SelfDestructListener(PluginManager pluginManager) {
        super(pluginManager);
    }

    public SelfDestructListener() {
        super();
    }

    @EventHandler
    public void on(BlockFromToEvent e) {
        this.throwEvent(e, e.getBlock(), e.getToBlock());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(BlockPistonExtendEvent e) {
        this.piston(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(BlockPistonRetractEvent e) {
        this.piston(e);
    }

    private void piston(BlockPistonEvent e) {
        Block firstBlock = e.getBlock();
        this.throwEvent(e, firstBlock, firstBlock.getRelative(e.getDirection()));
    }



    private void throwEvent(Cancellable e, Block block, Block toBlock) {
        Event event = new WorldSelfDestructEvent(e, block.getLocation(), toBlock.getLocation());
        this.throwEvent(event);
    }
}
