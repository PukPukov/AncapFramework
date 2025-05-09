package ru.ancap.framework.plugin.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import javax.annotation.Nullable;

public class DamageUtil {
    
    public @Nullable Player semiDeepFindDamager(Entity damager) {
        if (damager instanceof Player player) return player;
        else if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player player) return player;
            else return null;
        }
        return null;
    }
    
}