package ru.ancap.framework.api.center.command;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class PluginPrivateCommandRegistrar {
    
    private final JavaPlugin plugin;
    
    @Delegate
    private final OwnershipResolvingCommandRegistrar global;
    
    public CommandRegistrationBuilder register(String key) {
        return this.global.register(new OwnershipResolvingCommandRegistrar.Owner(this.plugin.getName()), key);
    }
    
}