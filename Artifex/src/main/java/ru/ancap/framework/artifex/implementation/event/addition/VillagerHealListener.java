package ru.ancap.framework.artifex.implementation.event.addition;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import ru.ancap.framework.api.event.events.wrapper.VillagerHealEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

public class VillagerHealListener extends ArtifexListener {

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (this.isHealingVillagerAt(event)) {
            this.throwEvent(new VillagerHealEvent(event, event.getPlayer(), (Villager) event.getRightClicked()));
        }
    }

    private boolean isHealingVillagerAt(PlayerInteractEntityEvent event) {
        return event.getRightClicked().getType() == EntityType.ZOMBIE_VILLAGER && 
               event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE;
    }
    
}
