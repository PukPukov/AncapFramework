package ru.ancap.framework.plugin.api.commands;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.plugin.api.AncapPlugin;

@RequiredArgsConstructor
public class PluginCommandRegistrar {
    
    private final AncapPlugin plugin;
    private final CommandCenter global;
    
    public CommandCenterDataPatch register(String key) {
        return this.global.register(key).link(this.plugin);
    }
    
    public CommandCenterDataPatch override(String key) {
        return this.global.override(key).link(this.plugin);
    }
    
    public CommandCenterDataPatch edit(String key) {
        return this.global.edit(key).link(this.plugin);
    }
    
}
