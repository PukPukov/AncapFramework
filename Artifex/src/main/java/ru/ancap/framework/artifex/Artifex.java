package ru.ancap.framework.artifex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.commons.time.Day;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.implementation.ancap.ArtifexAncap;
import ru.ancap.framework.artifex.implementation.command.center.AsyncCommandCenter;
import ru.ancap.framework.artifex.implementation.command.center.CommandCatcher;
import ru.ancap.framework.artifex.implementation.command.communicate.PlayerCommandFallback;
import ru.ancap.framework.artifex.implementation.event.addition.BlockClickListener;
import ru.ancap.framework.artifex.implementation.event.addition.VillagerHealListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ExplodeListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ProtectListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.SelfDestructListener;
import ru.ancap.framework.artifex.implementation.language.data.repository.SQLSpeakerModelRepository;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;
import ru.ancap.framework.artifex.implementation.language.flow.LanguageChangeListener;
import ru.ancap.framework.artifex.implementation.language.input.LAPIJoinListener;
import ru.ancap.framework.artifex.implementation.language.input.LanguageChangeInput;
import ru.ancap.framework.artifex.implementation.language.module.LanguageBase;
import ru.ancap.framework.artifex.implementation.language.module.LanguagesOperator;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.artifex.implementation.scheduler.SchedulerAPILoader;
import ru.ancap.framework.artifex.implementation.scheduler.SchedulerSilencer;
import ru.ancap.framework.artifex.implementation.timer.EveryDayTask;
import ru.ancap.framework.artifex.implementation.timer.TimerExecutor;
import ru.ancap.framework.artifex.implementation.timer.heartbeat.ArtifexHeartbeat;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.Communicator;
import ru.ancap.framework.database.sql.SQLDatabase;
import ru.ancap.framework.database.sql.connection.reader.DatabaseFromConfig;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.locale.MapLocales;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.PluginLoadTask;
import ru.ancap.framework.plugin.api.common.CommonMessageDomains;
import ru.ancap.framework.util.AudienceProvider;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@ToString
public final class Artifex extends AncapPlugin {

    public static JavaPlugin PLUGIN;

    @Getter
    private final List<Listener> listeners = List.of(
            new LanguageChangeListener()
    );
    
    private final List<Listener> eventApiListeners = List.of(
            new ProtectListener(),
            new SelfDestructListener(),
            new ExplodeListener(),
            new VillagerHealListener(),
            new BlockClickListener()
    );
    
    @Getter
    private final Map<String, CommandOperator> commands = Map.of(
            "language", new LanguageChangeInput(),
            "artifex", new ArtifexCommandExecutor(this.ancap)
    );
    
    @Getter
    private ArtifexAncap ancap;
    private CommandCatcher commandCatcher;
    private AsyncCommandCenter asyncCommandCenter;
    private SQLDatabase database;

    @Override
    public void onCoreLoad() {
        this.loadBukkitToKyori();
        this.loadCommonMessageDomains();
        this.loadAncap();
        this.loadConfiguration();
        this.loadDatabase();
        this.loadLAPI();
        this.loadLocales();
        this.loadTaskMeter();
        this.registerCommandCenter();
        this.startHeartbeat();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.loadInstance();
        this.registerIntegrators();
        this.loadSchedulerAPI();
        this.loadTimers();
        this.loadCommandAPI();
        this.loadEventAPI();
    }

    private void loadEventAPI() {
        if (ProtocolLibrary.getProtocolManager().getMinecraftVersion().compareTo(new MinecraftVersion("1.16.5")) < 0) return;
        this.eventApiListeners.forEach(this::registerEventsListener);
    }

    private void loadCommonMessageDomains() {
        CommonMessageDomains.pluginInfo = LAPIDomain.of(Artifex.class, "plugin-info");
    }

    private void loadInstance() {
        Artifex.PLUGIN = this;
    }

    private void loadBukkitToKyori() {
        AudienceProvider.bukkitAudiences(BukkitAudiences.create(this));
    }

    private void loadTaskMeter() {
        AncapPlugin.pluginLoadTaskProvider((plugin, callableMessage, runnable) -> new PluginLoadTask(
                plugin,
                callableMessage, 
                runnable, 
                LAPIDomain.of(Artifex.class, "console.notify.task.status.start"),
                LAPIDomain.of(Artifex.class, "console.notify.task.status.end")
        ));
    }

    private void loadTimers() {
        new TimerExecutor(this).run();
        AncapPlugin.scheduleSupport().upreg(
                "everyday-timer", 
                () -> AncapPlugin.scheduler().repeat(
                        EveryDayTask.class, 
                        ArtifexConfig.loaded().dayTimerAbsolute(),
                        Day.MILLISECONDS
                )
        );
    }

    private void loadCommandCatcher() {
        this.commandCatcher = new CommandCatcher(this.ancap, this, this.asyncCommandCenter, this.asyncCommandCenter);
    }

    private void loadCommandAPI() {
        this.loadCommandCatcher();
        this.registerEventsListener(this.commandCatcher);
        this.registerEventsListener(new PlayerCommandFallback());
        ProtocolLibrary.getProtocolManager().addPacketListener(this.commandCatcher);
    }

    private void registerCommandCenter() {
        this.asyncCommandCenter = new AsyncCommandCenter();
        this.registerCommandCenter(this.asyncCommandCenter);
    }

    private void loadAncap() {
        AncapBukkit.CORE_PLUGIN = this;
        ServerTPSCounter tpsCounter = new ServerTPSCounter(50);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, tpsCounter, 100L, 50L);
        this.ancap = new ArtifexAncap(tpsCounter);
        this.loadAncap(this.ancap);
    }

    @SneakyThrows
    private void loadSchedulerAPI() {
        SQLDatabase schedulerDatabase = new DatabaseFromConfig(
                this,
                ArtifexConfig.loaded().getSection().getConfigurationSection("database.scheduler-database")
        ).load();
        this.task("SchedulerAPI", new SchedulerAPILoader(
                new Communicator(Bukkit.getConsoleSender()),
                this,
                new Scanner(System.in),
                schedulerDatabase,
                new SchedulerSilencer(schedulerDatabase).load(),
                (scheduler, scheduleSupport) -> {
                    AncapPlugin.scheduler(scheduler);
                    AncapPlugin.scheduleSupport(scheduleSupport);
                }
        ));
    }

    private void loadDatabase() {
        this.database = new DatabaseFromConfig(
                this,
                ArtifexConfig.loaded().getSection().getConfigurationSection("database.main-database")
        ).load();
    }

    private void loadConfiguration() {
        new ArtifexConfig(this.getConfiguration("configuration.yml")).load();
    }

    @SneakyThrows
    private void loadLAPI() {
        SpeakerModelRepository repository = new SQLSpeakerModelRepository(this.database).load();
        this.registerEventsListener(
                new LAPIJoinListener(repository)
        );
        LAPI.setup(
                new MapLocales(ArtifexConfig.loaded().defaultLanguage()), 
                new LanguageBase(
                        new LanguagesOperator(repository),
                        ArtifexConfig.loaded().defaultLanguage()
                )
        );
    }

    private void startHeartbeat() {
        ArtifexHeartbeat heartbeat = new ArtifexHeartbeat(this);
        heartbeat.start();
    }
}
