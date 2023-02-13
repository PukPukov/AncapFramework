package ru.ancap.framework.plugin.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.commons.Cache;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.framework.plugin.api.configuration.StreamConfig;
import ru.ancap.util.resource.PluginResourceSource;
import ru.ancap.util.resource.ResourceSource;

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
        if (AncapMinimalisticPlugin.ancap == null) {
            throw new IllegalStateException("Ancap isn't loaded!");
        }
        AncapMinimalisticPlugin.ancap = null;
    }

    protected void loadAncap(Ancap ancap) {
        if (AncapMinimalisticPlugin.ancap != null) {
            throw new IllegalStateException("Ancap already loaded!");
        }
        AncapMinimalisticPlugin.ancap = ancap;
    }

    private void createPluginFolder() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
    }

    protected void loadLocale(String fileName) {
        new YamlLocaleLoader(
                new StreamConfig(
                        this.getResource(fileName)
                )
        ).run();
    }

    protected void registerEventsListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(
                listener,
                this
        );
    }
    
    private final Cache<ResourceSource> hard = new Cache<>(1_000_000_000_000L);

    protected ResourceSource getResourceSource() {
        return this.hard.get(() -> new PluginResourceSource(this, true));
    }

    private final Cache<ResourceSource> soft = new Cache<>(1_000_000_000_000L);

    protected ResourceSource getSoftResourceSource() {
        return this.soft.get(() -> new PluginResourceSource(this, false));
    }
}
