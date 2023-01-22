package ru.ancap.framework.api.plugin.plugins;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.plugin.plugins.commands.CommandCenter;
import ru.ancap.framework.api.plugin.plugins.config.StreamConfig;
import ru.ancap.framework.api.plugin.plugins.info.AncapPluginSettings;
import ru.ancap.framework.api.plugin.plugins.info.RegisterStage;
import ru.ancap.framework.api.plugin.plugins.language.locale.loader.LocaleLoader;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AncapPlugin extends AncapMinimalisticPlugin {

    private static final Map<String, AncapPlugin> plugins = new HashMap<>();
    private static CommandCenter commandCenter;
    private Metrics metrics;
    private AncapPluginSettings settings;
    protected Metrics getMetrics() {
        return this.metrics;
    }

    @MustBeInvokedByOverriders
    @Override
    public void onEnable() {
        super.onEnable();
        this.onCoreLoad();
        this.loadPluginSettings();
        this.registerMetrics();
        this.initializeInCommandCenter();
        this.autoRegisterIntegrators();
        this.register();
    }

    private void initializeInCommandCenter() {
        commandCenter.initialize(this);
    }

    protected void registerCommandCenter(CommandCenter commandCenter) {
        if (AncapPlugin.commandCenter != null) {
            throw new IllegalStateException("Command center is already registered!");
        }
        AncapPlugin.commandCenter = commandCenter;
    }

    protected void registerExecutor(String commandName, CommandOperator executor) {
        commandCenter.setExecutor(commandName, executor);
    }
    
    protected void registerIntegrators() {
        this.registerListeners();
        this.registerCommandExecutors();
    }
    
    private void autoRegisterIntegrators() {
        if (this.getSettings().getCommandExecutorRegisterStage() == RegisterStage.ANCAP_PLUGIN_ENABLE) {
            this.registerCommandExecutors();
        }
        if (this.getSettings().getListenerRegisterStage() == RegisterStage.ANCAP_PLUGIN_ENABLE) {
            this.registerListeners();
        }
    }

    protected void registerListeners() {
        for (Listener listener : this.getListeners()) {
            this.registerEventsListener(listener);
        }
    }

    protected void registerCommandExecutors() {
        for (Map.Entry<String, CommandOperator> command : this.getCommands().entrySet()) {
            this.registerExecutor(
                    command.getKey(),
                    command.getValue()
            );
        }
    }

    protected void loadLocales() {
        new LocaleLoader(
                this.getLogger(),
                this.getResourceSource()
        ).run();
    }

    protected Iterable<AncapPlugin> ancapPlugins() {
        return plugins.values();
    }

    private void register() {
        plugins.put(this.getName(), this);
    }

    private void unregister() {
        plugins.remove(this.getName());
    }

    @Override
    public void onDisable() {
        this.unregister();
    }

    public AncapPluginSettings getSettings() {
        return this.settings;
    }

    private void loadPluginSettings() {
        this.settings = new AncapPluginSettings(
                new StreamConfig(
                        this.getSoftResourceSource().getResource("ancapplugin.yml")
                )
        );
    }
    
    protected ConfigurationSection getConfig(String fileName) {
        return YamlConfiguration.loadConfiguration(
                new InputStreamReader(
                        this.getResourceSource().getResource(fileName)
                )
        );
    }

    protected void registerMetrics() {
        this.metrics = new Metrics(this, this.getPluginId());
        // metrics.enable();
    }

    protected int getPluginId() {
        return this.getSettings().getPluginId();
    }
    
    protected Map<String, CommandOperator> getCommands() {
        return Map.of();
    }

    protected List<Listener> getListeners() {
        return List.of();
    }

}
