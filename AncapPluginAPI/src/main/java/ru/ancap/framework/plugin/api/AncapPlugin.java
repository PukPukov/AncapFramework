package ru.ancap.framework.plugin.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.commons.TriFunction;
import ru.ancap.commons.cache.Cache;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.configuration.AnnotationConfiguration;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;
import ru.ancap.framework.plugin.api.information.AncapPluginSettings;
import ru.ancap.framework.plugin.api.information.RegisterStage;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleHandle;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleLoader;
import ru.ancap.framework.resource.config.BuiltTransferMap;
import ru.ancap.framework.resource.config.FileConfigurationPreparator;
import ru.ancap.scheduler.Scheduler;
import ru.ancap.scheduler.support.ScheduleSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Accessors(fluent = true) @Getter
public abstract class AncapPlugin extends AncapMinimalisticPlugin {

    private static final Map<String, AncapPlugin> plugins = new HashMap<>();
    protected static CommandExecutor proxy;
    private static volatile CommandCenter commandCenter;
    private static Scheduler scheduler;
    private static ScheduleSupport scheduleSupport;
    private static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider;
    private AncapPluginSettings settings;
    private PluginCommandRegistrar commandRegistrar;
    private LocaleHandle localeHandle;
    
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
        Set<String> registeredCommands = Set.copyOf(this.commandCenter().findRegisteredCommandsOf(this));
        registeredCommands.forEach(id -> {
            this.commandRegistrar.unregister(id);
        });
        LAPI.drop(this.lapiSection());
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
    
    public void registerIntegrators() {
        this.registerListeners();
        this.registerCommandExecutors();
    }
    
    private void autoRegisterIntegrators() {
        RegisterStage executors = this.settings().commandExecutorRegisterStage();
        RegisterStage listeners = this.settings().listenerRegisterStage();
        if (executors == RegisterStage.ANCAP_PLUGIN_ENABLE) this.registerCommandExecutors();
        if (listeners == RegisterStage.ANCAP_PLUGIN_ENABLE) this.registerListeners();
        if (
            this.settings().mainListenerRegisterStage() == RegisterStage.ANCAP_PLUGIN_ENABLE &&
            this instanceof Listener listener
        ) Bukkit.getPluginManager().registerEvents(listener, this);
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
        this.localeHandle = new LocaleLoader(
            this.getLogger(),
            this.newResourceSource(FileConfigurationPreparator.resolveConflicts(
                (version) -> this.valueTransferMap() != null ?
                    BuiltTransferMap.makeFor(this.valueTransferMap().getConfigurationSection("custom.LanguageAPI"), version) :
                    BuiltTransferMap.EMPTY,
                "version"
            )),
            this.lapiSection()
        ).load();
    }
    
    public String lapiSection() {
        return this.getDescription().getName();
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
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public @NonNull AncapPluginSettings getSettings() {
        return this.settings();
    }
    
    private void loadPluginSettings() {
        this.settings = new AncapPluginSettings(this.newResourceSource(FileConfigurationPreparator.internal()).getResource("ancapplugin.yml"));
    }
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public ConfigurationSection getConfiguration() {
        return this.configuration();
    }
    
    public ConfigurationSection configuration() {
        return this.configuration("configuration.yml");
    }
    
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public ConfigurationSection getConfiguration(String fileName) {
        return this.configuration(fileName);
    }
    
    public ConfigurationSection configuration(String fileName) {
        return this.newResourceSource(FileConfigurationPreparator.resolveConflicts(
            (version) -> this.valueTransferMap() != null ? 
                BuiltTransferMap.makeFor(this.valueTransferMap().getConfigurationSection("main-domain."+fileName), version) :
                BuiltTransferMap.EMPTY,
            "configuration-version"
        )).getResource(fileName);
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
     * Start executing of the metered task. 
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