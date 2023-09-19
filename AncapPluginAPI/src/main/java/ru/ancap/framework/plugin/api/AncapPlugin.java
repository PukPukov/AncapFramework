package ru.ancap.framework.plugin.api;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import ru.ancap.commons.TriFunction;
import ru.ancap.commons.cache.Cache;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.configuration.AnnotationConfiguration;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;
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
import java.util.logging.Logger;

public abstract class AncapPlugin extends AncapMinimalisticPlugin {

    private static final Map<String, AncapPlugin> plugins = new HashMap<>();
    protected static CommandExecutor proxy;
    private static CommandCenter commandCenter;
    private static Scheduler scheduler;
    private static ScheduleSupport scheduleSupport;
    private static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider;
    private AncapPluginSettings settings;
    private PluginCommandRegistrar commandRegistrar;
    
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
        this.loadLocales();
        this.loadPluginCommandRegistrar();
        this.loadPluginSettings();
        this.initializeInCommandCenter();
        this.autoRegisterIntegrators();
        this.register();
    }
    
    @MustBeInvokedByOverriders
    @Override
    public void onDisable() {
        this.unregister();
        this.commandCenter().findRegisteredCommandsOf(this).forEach(id -> this.commandRegistrar.unregister(id));
    }

    private void loadPluginCommandRegistrar() {
        this.commandRegistrar = new PluginCommandRegistrar(this, AncapPlugin.commandCenter);
    }

    public void loadAnnotationConfiguration(Class<?> config) {
        AnnotationConfiguration.load(config, this.getConfiguration());
    }

    private void initializeInCommandCenter() {
        AncapPlugin.commandCenter.initialize(this);
    }

    public void registerCommandCenter(CommandCenter commandCenter) {
        if (AncapPlugin.commandCenter != null) throw new IllegalStateException("Command center is already registered!");
        AncapPlugin.commandCenter = commandCenter;
    }

    public PluginCommandRegistrar commandRegistrar() {
        return this.commandRegistrar;
    }

    /**
     * <b>Deprecated</b>, use {@link AncapPlugin#commandRegistrar}
     */
    @Deprecated(forRemoval = true)
    public void registerExecutor(String commandName, CommandOperator executor) {
        AncapPlugin.commandCenter.setExecutor(commandName, executor);
    }

    public void registerIntegrators() {
        this.registerListeners();
        this.registerCommandExecutors();
    }
    
    private void autoRegisterIntegrators() {
        RegisterStage executors = this.getSettings().commandExecutorRegisterStage();
        RegisterStage listeners = this.getSettings().listenerRegisterStage();
        if (executors == RegisterStage.ANCAP_PLUGIN_ENABLE) this.registerCommandExecutors();
        if (listeners == RegisterStage.ANCAP_PLUGIN_ENABLE) this.registerListeners();
    }

    public void registerListeners() {
        for (Listener listener : this.listeners()) this.registerEventsListener(listener);
    }

    public void registerCommandExecutors() {
        for (Map.Entry<String, CommandOperator> entry : this.commands().entrySet()) {
            this.commandRegistrar().register(entry.getKey(), entry.getValue());
        }
    }

    public void loadLocales() {
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

    public Iterable<AncapPlugin> ancapPlugins() {
        return plugins.values();
    }

    private void register() {
        plugins.put(this.getName(), this);
    }

    private void unregister() {
        plugins.remove(this.getName());
    }

    private CommandCenter commandCenter() {
        return commandCenter;
    }

    public @NotNull AncapPluginSettings getSettings() {
        return this.settings;
    }

    private void loadPluginSettings() {
        this.settings = new AncapPluginSettings(this.newResourceSource(FileConfigurationPreparator.internal()).getResource("ancapplugin.yml"));
    }

    public ConfigurationSection getConfiguration() {
        return this.getConfiguration("configuration.yml");
    }

    private final Cache<FileConfiguration> configCache = new Cache<>();

    public ConfigurationSection getConfiguration(String fileName) {
        return this.configCache.get(() -> this.newResourceSource(FileConfigurationPreparator.resolveConflicts(
            (version) -> this.valueTransferMap() != null ? 
                BuiltTransferMap.makeFor(this.valueTransferMap().getConfigurationSection("main-domain."+fileName), version) :
                BuiltTransferMap.EMPTY,
            "configuration-version"
        )).getResource(fileName));
    }
    
    private final Cache<ConfigurationSection> valueTransferMapCache = new Cache<>();

    public ConfigurationSection valueTransferMap() {
        return this.valueTransferMapCache.get(() -> this.newResourceSource(FileConfigurationPreparator.internal()).getResource("value-transfer-map.yml"));
    }

    public Map<String, CommandOperator> commands() {
        return Map.of();
    }

    public List<Listener> listeners() {
        return List.of();
    }

    /**
     * Start executing of metered task. 
     */
    public void task(CallableMessage taskName, Runnable task) {
        AncapPlugin.pluginLoadTaskProvider().apply(this, taskName, task).run();
    }

    /**
     * Start executing of metered task. 
     */
    public void task(String taskName, Runnable task) {
        this.task(new Message(taskName), task);
    }
    
    public Logger newInternalLogger(String name) {
        return new PluginLoggerCreator().create(this, name);
    }
    
}
