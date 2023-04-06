package ru.ancap.framework.plugin.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.commons.resource.ResourceSource;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.framework.plugin.api.configuration.StreamConfig;
import ru.ancap.framework.resource.PluginResourceSource;
import ru.ancap.framework.resource.ResourcePreparator;

public abstract class AncapMinimalisticPlugin extends JavaPlugin {

    private static Ancap ancap;

    @MustBeInvokedByOverriders
    @Override
    public void onEnable() {
        super.onEnable();
        this.createPluginFolder();
    }

    protected void onCoreLoad() {
        // to override it in core plugin
    }

    protected Ancap getAncap() {
        return ancap;
    }

    protected void unloadAncap() {
        if (AncapMinimalisticPlugin.ancap == null) throw new IllegalStateException("Ancap isn't loaded!");
        AncapMinimalisticPlugin.ancap = null;
    }

    protected void loadAncap(Ancap ancap) {
        if (AncapMinimalisticPlugin.ancap != null) throw new IllegalStateException("Ancap already loaded!");
        AncapMinimalisticPlugin.ancap = ancap;
    }

    private void createPluginFolder() {
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdirs();
    }

    protected void loadLocale(String fileName) {
        new YamlLocaleLoader(new StreamConfig(this.getResource(fileName))).run();
    }

    protected void registerEventsListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    protected <T> ResourceSource<T> newResourceSource(ResourcePreparator<T> resourcePreparator) {
        return new PluginResourceSource<>(this, resourcePreparator);
    }
    
}
