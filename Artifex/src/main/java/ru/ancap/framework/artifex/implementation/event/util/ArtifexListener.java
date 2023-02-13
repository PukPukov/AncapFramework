package ru.ancap.framework.artifex.implementation.event.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public abstract class ArtifexListener implements Listener {

    private final PluginManager pluginManager;

    public ArtifexListener() {
        this.pluginManager = Bukkit.getPluginManager();
    }

    public ArtifexListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    protected PluginManager getPluginManager() {
        return this.pluginManager;
    }

    protected void throwEvent(Event event) {
        this.pluginManager.callEvent(event);
    }

}
