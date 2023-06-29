package ru.ancap.framework.identifier;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Identifier {
    
    private static final IdentifyStrategy STRATEGY;

    static  {
        if (Bukkit.getOnlineMode()) STRATEGY = new IdentifyStrategy() {
            @Override public String identify(CommandSender sender) {
                if (sender instanceof Player player) return this.identify(player);
                return sender.getName().toLowerCase();
            }
            @Override public String identify(Player player) {           return player.getUniqueId().toString();               }
            @Override public @Nullable Player find(String identifier) { return Bukkit.getPlayer(UUID.fromString(identifier)); }
        };
        else STRATEGY = new IdentifyStrategy() {
            @Override public String identify(CommandSender sender) {    return sender.getName().toLowerCase(); }
            @Override public String identify(Player player) {           return player.getName().toLowerCase(); }
            @Override public @Nullable Player find(String identifier) { return Bukkit.getPlayer(identifier);   }
        };
    }
    
    public static String of(CommandSender sender) {
        return STRATEGY.identify(sender);
    }
    
    public static String of(Player player) {
        return STRATEGY.identify(player);
    }
    
    public static @Nullable Player onlinePlayerFor(String identifier) {
        return STRATEGY.find(identifier);
    }

    private interface IdentifyStrategy {
        
        String identify(CommandSender sender);
        String identify(Player player);
        @Nullable Player find(String identifier);
        
    }
    
}
