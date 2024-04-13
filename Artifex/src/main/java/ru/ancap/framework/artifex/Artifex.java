package ru.ancap.framework.artifex;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ancap.commons.instructor.SimpleEventBus;
import ru.ancap.commons.map.MapGC;
import ru.ancap.commons.time.Day;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.implementation.ancap.ArtifexAncap;
import ru.ancap.framework.artifex.implementation.command.center.AsyncCommandCenter;
import ru.ancap.framework.artifex.implementation.command.center.CommandProxy;
import ru.ancap.framework.artifex.implementation.command.communicate.PlayerCommandFallback;
import ru.ancap.framework.artifex.implementation.communicator.message.clickable.ActionProxy;
import ru.ancap.framework.artifex.implementation.event.addition.BlockClickListener;
import ru.ancap.framework.artifex.implementation.event.addition.VillagerHealListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ExplodeListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ProtectListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.SelfDestructListener;
import ru.ancap.framework.artifex.implementation.language.data.repository.SQLSpeakerModelRepository;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;
import ru.ancap.framework.artifex.implementation.language.domains.common.ArtifexCommonMessageDomains;
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
import ru.ancap.framework.artifex.status.tests.CommandCenterTest;
import ru.ancap.framework.artifex.status.tests.ConfigurationDatabaseTest;
import ru.ancap.framework.artifex.status.tests.ConfigurationTest;
import ru.ancap.framework.artifex.status.tests.LAPITest;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.database.sql.SQLDatabase;
import ru.ancap.framework.database.sql.connection.reader.DatabaseFromConfig;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.locale.MapLocales;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.PluginLoadTask;
import ru.ancap.framework.status.test.Test;
import ru.ancap.framework.util.AudienceProvider;
import ru.ancap.framework.util.player.StepbackMaster;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@ToString
@Accessors(fluent = true)
public final class Artifex extends AncapPlugin {

    @Getter
    private static AncapPlugin PLUGIN;

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

    @Override
    public Map<String, CommandOperator> commands() {
        return Map.of(
            "language", new LanguageChangeInput(), 
            "artifex",  new ArtifexCommandExecutor(this.ancap, this.tests, this.localeHandle())
        );
    }
    
    @Getter
    private ArtifexAncap ancap;
    private AsyncCommandCenter asyncCommandCenter;
    private SQLDatabase database;
    private List<Test> tests;
    private ServerTPSCounter tpsCounter;
    private StepbackMaster stepbackMaster;
    private SimpleEventBus<Player> playerLeaveInstructor;

    @Override
    public void onCoreLoad() {
        this.loadBukkitToKyori();
        this.loadCommonMessageDomains();
        this.loadTPSCounter();
        this.loadPlayerLeaveInstructor();
        this.loadStepbackMaster();
        this.loadAncap();
        this.loadConfiguration();
        this.loadDatabase();
        this.loadLAPI();
        this.loadLocales();
        this.loadTaskMeter();
        this.loadCommandModule();
        this.startHeartbeat();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.loadMetrics();
        this.loadInstance();
        this.loadSchedulerAPI();
        this.loadTimers();
        this.loadEventAPI();
        this.loadClickableMessageActionProxy();
        this.loadTests();
        this.registerIntegrators();
    }

    private void loadMetrics() {
        new Metrics(this, 14261);
    }

    private void loadPlayerLeaveInstructor() {
        this.playerLeaveInstructor = new SimpleEventBus<>();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void on(PlayerQuitEvent event) {
                Artifex.this.playerLeaveInstructor.dispatch(event.getPlayer());
            }
        }, this);
    }

    private void loadStepbackMaster() {
        this.stepbackMaster = new StepbackMaster(
            this,
            this.playerLeaveInstructor
                .map(Identifier::of)
                .as(MapGC::new),
            3,
            15
        );
        this.stepbackMaster.run();
    }

    private void loadTPSCounter() {
        this.tpsCounter = new ServerTPSCounter(50);
        this.tpsCounter.startWith(this);
    }

    private void loadTests() {
        this.tests = List.of(
            new ConfigurationDatabaseTest(),
            new CommandCenterTest(this.commandRegistrar()),
            new LAPITest(this),
            new ConfigurationTest(this)
        );
    }

    private void loadClickableMessageActionProxy() {
        new ActionProxy("cmap").setup(this.commandRegistrar());
    }

    private void loadEventAPI() {
        if (ProtocolLibrary.getProtocolManager().getMinecraftVersion().compareTo(new MinecraftVersion("1.16.5")) < 0) return;
        this.eventApiListeners.forEach(this::registerEventsListener);
    }

    private void loadCommonMessageDomains() {
        new ArtifexCommonMessageDomains().load();
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

    private void loadCommandModule() {
        AncapPlugin.proxy = new CommandProxy();
        this.asyncCommandCenter = new AsyncCommandCenter(AncapPlugin.proxy);
        this.registerCommandCenter(this.asyncCommandCenter);
        this.ancap.installGlobalCommandOperator(this, this.asyncCommandCenter, this.asyncCommandCenter);
        this.registerEventsListener(new PlayerCommandFallback());
    }

    private void loadAncap() {
        AncapBukkit.CORE_PLUGIN = this;
        this.ancap = new ArtifexAncap(this.tpsCounter, this.stepbackMaster, this.debugIndicatorFile());
        this.ancap.load();
        this.loadAncap(this.ancap);
    }

    private File debugIndicatorFile() {
        File folder = new File(this.getDataFolder().getParentFile().getParentFile(), "debug");
        if (!folder.exists()) folder.mkdirs();
        return new File(folder, "debug.indicator");
    }

    @SneakyThrows
    private void loadSchedulerAPI() {
        SQLDatabase schedulerDatabase = new DatabaseFromConfig(
            this,
            ArtifexConfig.loaded().getSection().getConfigurationSection("database.scheduler-database")
        ).load();
        this.task("SchedulerAPI", new SchedulerAPILoader(
            Communicator.of(Bukkit.getConsoleSender()),
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
        new ArtifexConfig(this.configuration("configuration.yml")).load();
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