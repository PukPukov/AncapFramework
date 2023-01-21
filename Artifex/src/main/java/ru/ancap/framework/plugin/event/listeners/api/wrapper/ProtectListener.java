package ru.ancap.framework.plugin.event.listeners.api.wrapper;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.projectiles.ProjectileSource;
import ru.ancap.framework.api.event.events.additions.BlockClickEvent;
import ru.ancap.framework.api.event.events.wrapper.PVPEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldInteractEvent;
import ru.ancap.framework.api.event.events.wrapper.WorldSelfDestructEvent;
import ru.ancap.framework.plugin.event.listeners.ArtifexListener;

import java.util.Collection;

public class ProtectListener extends ArtifexListener {

    public ProtectListener(PluginManager pluginManager) {
        super(pluginManager);
    }

    public ProtectListener() {
        super();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerBucketEmptyEvent e) {
        this.bucket(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerBucketFillEvent e) {
        this.bucket(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerBucketEntityEvent e) {
        this.throwEvent(e, e.getPlayer(), e.getEntity().getLocation());
    }

    private void bucket(PlayerBucketEvent e) {
        this.throwEvent(
                new WorldInteractEvent(e, e.getPlayer(), e.getBlock().getLocation())
        );
        this.throwEvent(
                new WorldInteractEvent(e, e.getPlayer(), e.getBlockClicked().getLocation())
        );
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(EntityDamageByEntityEvent e) {
        Entity eDamaged = e.getEntity();
        Entity eDamager = e.getDamager();
        Event event = new WorldSelfDestructEvent(e, eDamaged.getLocation(), eDamager.getLocation());
        this.throwEvent(event);
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
        this.throwEvent(e, damager, location);
        Player damaged;
        if (!(eDamaged instanceof Player)) {
            return;
        }
        damaged = (Player) eDamaged;
        Event event1 = new PVPEvent(e, damaged, damager);
        this.throwEvent(event1);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(BlockBreakEvent e) {
        this.throwEvent(e, e.getPlayer(), e.getBlock().getLocation());
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(BlockPlaceEvent e) {
        this.throwEvent(e, e.getPlayer(), e.getBlock().getLocation());
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(BlockClickEvent e) {
        this.throwEvent(e, e.getClicker(), e.getBlock().getLocation());
    }
    @EventHandler (priority = EventPriority.LOW)
    public void on(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Location interacted = e.getLocation();
        this.throwEvent(e, player, interacted);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        ProjectileSource source = projectile.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        Block block = e.getHitBlock();
        Entity entity = e.getHitEntity();
        Location interacted;
        if (block != null) {
            interacted = block.getLocation();
        } else if (entity != null) {
            interacted = entity.getLocation();
        } else {
            return;
        }
        this.throwEvent(e, player, interacted);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void on(PotionSplashEvent e) {
        ThrownPotion potion = e.getPotion();
        Collection<LivingEntity> entities = e.getAffectedEntities();
        if (entities.size() == 0) {
            return;
        }
        ProjectileSource source = potion.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        for (Entity entity : entities) {
            Location interacted = entity.getLocation();
            this.throwEvent(e, player, interacted);
            if (entity instanceof Player) {
                Player damaged = (Player) entity;
                Event event = new PVPEvent(e, player, damaged);
                this.throwEvent(event);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        this.throwEvent(e, e.getPlayer(), e.getRightClicked().getLocation());
    }

    private void throwEvent(Cancellable e, Player player, Location interacted) {
        this.throwEvent(
                new WorldInteractEvent(
                        e,
                        player,
                        interacted)
        );
    }
}
