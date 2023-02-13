package ru.ancap.framework.artifex.implementation.event.wrapper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;
import ru.ancap.framework.api.event.events.additions.BlockClickEvent;
import ru.ancap.framework.api.event.events.additions.BlockNullifyEvent;
import ru.ancap.framework.api.event.events.wrapper.PVPEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.artifex.implementation.event.util.ArtifexListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProtectListener extends ArtifexListener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PlayerBucketEmptyEvent event) {
        this.bucket(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PlayerBucketFillEvent event) {
        this.bucket(event);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PlayerBucketEntityEvent event) {
        this.throwEvent(new WorldInteractEvent(event, event.getPlayer(), List.of(event.getEntity().getLocation())));
    }

    private void bucket(PlayerBucketEvent event) {
        this.throwEvent(new WorldInteractEvent(event, event.getPlayer(), List.of(event.getBlock().getLocation(), event.getBlockClicked().getLocation())));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent event) {
        Entity eDamaged = event.getEntity();
        Entity eDamager = event.getDamager();
        this.throwEvent(new WorldSelfDestructEvent(event, eDamager.getLocation(), List.of(eDamaged.getLocation())));
        Location location = eDamaged.getLocation();
        Player damager;
        if (eDamager instanceof Player){
            damager = (Player) eDamager;
        } else if (eDamager instanceof Projectile) {
            Projectile projectile = (Projectile) eDamager;
            ProjectileSource source = projectile.getShooter();
            if (!(source instanceof Player)) {
                return;
            }
            damager = (Player) source;
        } else {
            return;
        }
        if (eDamaged instanceof Monster || eDamaged instanceof Boss) {
            return;
        }
        this.throwEvent(new WorldInteractEvent(event, damager, List.of(location)));
        Player damaged;
        if (!(eDamaged instanceof Player)) {
            return;
        }
        damaged = (Player) eDamaged;
        Event event1 = new PVPEvent(event, damager, List.of(damaged));
        this.throwEvent(event1);
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockBreakEvent e) {
        this.throwEvent(new WorldInteractEvent(e, e.getPlayer(), List.of(e.getBlock().getLocation())));
        this.throwEvent(new BlockNullifyEvent(e, List.of(e.getBlock()), false));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockPlaceEvent event) {
        this.throwEvent(new WorldInteractEvent(event, event.getPlayer(), List.of(event.getBlock().getLocation())));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(BlockClickEvent event) {
        this.throwEvent(new WorldInteractEvent(event, event.clicker(), List.of(event.block().getLocation())));
    }
    
    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Location interacted = event.getLocation();
        this.throwEvent(new WorldInteractEvent(event, player, List.of(interacted)));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Block block = event.getHitBlock();
        Entity entity = event.getHitEntity();
        Location interacted;
        if (block != null) {
            interacted = block.getLocation();
        } else if (entity != null) {
            interacted = entity.getLocation();
        } else {
            return;
        }
        this.throwEvent(new WorldInteractEvent(event, player, List.of(interacted)));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void on(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        Collection<LivingEntity> entities = event.getAffectedEntities();
        if (entities.size() == 0) {
            return;
        }
        ProjectileSource source = potion.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        List<Player> damagedPlayers = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        for (Entity entity : entities) {
            locations.add(entity.getLocation());
            if (entity instanceof Player) damagedPlayers.add((Player) entity);
        }
        this.throwEvent(new WorldInteractEvent(event, player, locations));
        if (damagedPlayers.size() != 0) this.throwEvent(new PVPEvent(event, player, damagedPlayers));
    }

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        this.throwEvent(new WorldInteractEvent(event, event.getPlayer(), List.of(event.getRightClicked().getLocation())));
    }
}
