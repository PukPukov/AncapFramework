package ru.ancap.framework.plugin.api;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.commons.TriFunction;
import ru.ancap.commons.cache.Cache;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.configuration.AnnotationConfiguration;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.information.AncapPluginSettings;
import ru.ancap.framework.plugin.api.information.RegisterStage;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleLoader;
import ru.ancap.framework.resource.config.BuiltTransferMap;
import ru.ancap.framework.resource.config.FileConfigurationPreparator;
import ru.ancap.scheduler.Scheduler;
import ru.ancap.scheduler.support.ScheduleSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AncapPlugin extends AncapMinimalisticPlugin {

    private static final Map<String, AncapPlugin> plugins = new HashMap<>();
    protected static CommandExecutor proxy;
    private static CommandCenter commandCenter;
    private static Scheduler scheduler;
    private static ScheduleSupport scheduleSupport;
    private static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider;
    private Metrics metrics;
    private AncapPluginSettings settings;

    protected Metrics getMetrics() {
        return this.metrics;
    }
    
    public static Scheduler scheduler() { return AncapPlugin.scheduler; }
    public static void scheduler(Scheduler scheduler) { AncapPlugin.scheduler = scheduler; }
    public static ScheduleSupport scheduleSupport() { return AncapPlugin.scheduleSupport; }
    public static void scheduleSupport(ScheduleSupport scheduleSupport) { AncapPlugin.scheduleSupport = scheduleSupport; }
    public static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider() { return AncapPlugin.pluginLoadTaskProvider; }
    public static void pluginLoadTaskProvider(TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider) { AncapPlugin.pluginLoadTaskProvider = pluginLoadTaskProvider; }

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
    
    protected void loadAnnotationConfiguration(Class<?> config) {
        AnnotationConfiguration.load(config, this.getConfiguration());
    }

    private void initializeInCommandCenter() {
        AncapPlugin.commandCenter.initialize(this);
    }

    protected void registerCommandCenter(CommandCenter commandCenter) {
        if (AncapPlugin.commandCenter != null) throw new IllegalStateException("Command center is already registered!");
        AncapPlugin.commandCenter = commandCenter;
    }

    protected void registerExecutor(String commandName, CommandOperator executor) {
        AncapPlugin.commandCenter.setExecutor(commandName, executor);
        if (AncapPlugin.proxy != null) Bukkit.getPluginCommand(commandName).setExecutor(AncapPlugin.proxy);
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
            this.registerExecutor(command.getKey(), command.getValue());
        }
    }

    protected void loadLocales() {
        new LocaleLoader(
            this.getLogger(),
            this.newResourceSource(FileConfigurationPreparator.resolveConflicts(
                (version) -> this.valueTransferMap() != null ?
                    BuiltTransferMap.makeFor(this.valueTransferMap().getConfigurationSection("custom.LanguageAPI"), version) :
                    BuiltTransferMap.EMPTY,
                "version"
            ))
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
        this.settings = new AncapPluginSettings(this.newResourceSource(FileConfigurationPreparator.internal()).getResource("ancapplugin.yml"));
    }
    
    protected ConfigurationSection getConfiguration() {
        return this.getConfiguration("configuration.yml");
    }

    private final Cache<FileConfiguration> configCache = new Cache<>();
    
    protected ConfigurationSection getConfiguration(String fileName) {
        return this.configCache.get(() -> this.newResourceSource(FileConfigurationPreparator.resolveConflicts(
            (version) -> this.valueTransferMap() != null ? 
                BuiltTransferMap.makeFor(this.valueTransferMap().getConfigurationSection("main-domain."+fileName), version) :
                BuiltTransferMap.EMPTY,
            "configuration-version"
        )).getResource(fileName));
    }
    
    private final Cache<ConfigurationSection> valueTransferMapCache = new Cache<>();

    protected ConfigurationSection valueTransferMap() {
        return this.valueTransferMapCache.get(() -> this.newResourceSource(FileConfigurationPreparator.internal()).getResource("value-transfer-map.yml"));
    }

    protected void registerMetrics() {
        this.metrics = new Metrics(this, this.getPluginIdentifier());
        // metrics.enable();
    }

    protected int getPluginIdentifier() {
        return this.getSettings().getPluginIdentifier();
    }
    
    protected Map<String, CommandOperator> getCommands() {
        return Map.of();
    }

    protected List<Listener> getListeners() {
        return List.of();
    }

    /**
     * Start executing of metered task. 
     */
    protected void task(CallableMessage taskName, Runnable task) {
        AncapPlugin.pluginLoadTaskProvider().apply(this, taskName, task).run();
    }

    /**
     * Start executing of metered task. 
     */
    protected void task(String taskName, Runnable task) {
        this.task(new Message(taskName), task);
    }
    
}
