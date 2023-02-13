package ru.ancap.framework.plugin.api;

import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import ru.ancap.commons.TriFunction;
import ru.ancap.communicate.message.CallableMessage;
import ru.ancap.communicate.message.Message;
import ru.ancap.config.AnnotationConfig;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.configuration.StreamConfig;
import ru.ancap.framework.plugin.api.information.AncapPluginSettings;
import ru.ancap.framework.plugin.api.information.RegisterStage;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleLoader;
import ru.ancap.scheduler.Scheduler;
import ru.ancap.scheduler.support.ScheduleSupport;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AncapPlugin extends AncapMinimalisticPlugin {

    private static final Map<String, AncapPlugin> plugins = new HashMap<>();
    private static CommandCenter commandCenter;
    private static Scheduler scheduler;
    private static ScheduleSupport scheduleSupport;
    private static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider;

    private static Function<JavaPlugin, CommandOperator> authorsSupplierFactory;
    private static BiFunction<JavaPlugin, String, CommandOperator> customAuthorsSupplierFactory;
    private Metrics metrics;
    private AncapPluginSettings settings;

    protected Metrics getMetrics() {
        return this.metrics;
    }
    
    public static Scheduler scheduler() {return AncapPlugin.scheduler;}
    public static void scheduler(Scheduler scheduler) {AncapPlugin.scheduler = scheduler;}
    public static ScheduleSupport scheduleSupport() {return AncapPlugin.scheduleSupport;}
    public static void scheduleSupport(ScheduleSupport scheduleSupport) {AncapPlugin.scheduleSupport = scheduleSupport;}
    public static TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider() {return AncapPlugin.pluginLoadTaskProvider;}
    public static void authorsSupplierFactory(Function<JavaPlugin, CommandOperator> factory) {AncapPlugin.authorsSupplierFactory = factory;}
    public static void customAuthorsSupplierFactory(BiFunction<JavaPlugin, String, CommandOperator> factory) {AncapPlugin.customAuthorsSupplierFactory = factory;}
    
    public static void pluginLoadTaskProvider(TriFunction<JavaPlugin, CallableMessage, Runnable, PluginLoadTask> pluginLoadTaskProvider) {AncapPlugin.pluginLoadTaskProvider = pluginLoadTaskProvider;}

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
    
    protected void loadAnnotationConfig(Class<?> config) {
        this.saveDefaultConfig();
        AnnotationConfig.load(config, this.getConfig());
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
        AncapPlugin.commandCenter.setExecutor(commandName, executor);
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
    
    protected CommandOperator authorsSupplier() {
        return AncapPlugin.authorsSupplierFactory.apply(this);
    }

    protected CommandOperator authorsSupplier(String customMessage) {
        return AncapPlugin.customAuthorsSupplierFactory.apply(this, customMessage);
    }
    
    protected ConfigurationSection getConfig(String fileName) {
        return YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResourceSource().getResource(fileName)));
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
