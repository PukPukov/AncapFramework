package ru.ancap.util;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class AudienceProvider {
    
    
    @Getter @Setter
    private static BukkitAudiences bukkitAudiences;
    
}
