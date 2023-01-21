package ru.ancap.framework.plugin.event.listeners.api.addition;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import ru.ancap.framework.api.event.events.additions.BlockClickEvent;
import ru.ancap.framework.plugin.event.listeners.ArtifexListener;

public class BlockClickListener extends ArtifexListener {

    public BlockClickListener(PluginManager pluginManager) {
        super(pluginManager);
    }

    public BlockClickListener() {
        super();
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        BlockClickEvent event = new BlockClickEvent(e, e.getClickedBlock(), e.getPlayer());
        this.throwEvent(event);
    }
}
