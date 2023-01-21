package ru.ancap.framework.plugin.event.listeners.api.addition;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.PluginManager;
import ru.ancap.framework.api.event.events.wrapper.VillagerHealEvent;
import ru.ancap.framework.plugin.event.listeners.ArtifexListener;

public class VillagerHealListener extends ArtifexListener {

    public VillagerHealListener(PluginManager pluginManager) {
        super(pluginManager);
    }

    public VillagerHealListener() {
        super();
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (this.isHealingVillagerAt(e)) {
            this.throwEvent(e, e.getPlayer(), (Villager) e.getRightClicked());
        }
    }

    private boolean isHealingVillagerAt(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ZOMBIE_VILLAGER && e.getPlayer().getItemInHand().getType() == Material.GOLDEN_APPLE) {
            return true;
        }
        return false;
    }

    private void throwEvent(PlayerInteractEntityEvent e, Player player, Villager rightClicked) {
        this.throwEvent(new VillagerHealEvent(e, player, rightClicked));
    }
}
