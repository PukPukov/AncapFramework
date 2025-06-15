package ru.ancap.framework.plugin.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DamageUtil {
    
    public static @Nullable DeepDamager semiDeepFindDamager(Entity damager) {
        if (damager instanceof Player player) return new DeepDamager(player, null);
        else if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player player) return new DeepDamager(player, null);
            else return null;
        }
        return null;
    }
    
    public record DeepDamager(@NotNull Player player, @Nullable Projectile carrier) { }
    
}