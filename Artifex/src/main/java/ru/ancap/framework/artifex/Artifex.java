package ru.ancap.framework.artifex;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import ru.ancap.commons.instructor.SimpleEventBus;
import ru.ancap.commons.map.MapGC;
import ru.ancap.commons.time.Day;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.exception.operator.*;
import ru.ancap.framework.artifex.implementation.ancap.ArtifexAncap;
import ru.ancap.framework.artifex.implementation.command.center.AsyncCommandCenter;
import ru.ancap.framework.artifex.implementation.command.center.CommandProxy;
import ru.ancap.framework.artifex.implementation.common.ArtifexCommonMessageDomains;
import ru.ancap.framework.artifex.implementation.communicator.message.clickable.ActionProxy;
import ru.ancap.framework.artifex.implementation.event.addition.BlockClickListener;
import ru.ancap.framework.artifex.implementation.event.addition.VillagerHealListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ExplodeListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.ProtectListener;
import ru.ancap.framework.artifex.implementation.event.wrapper.SelfDestructListener;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.artifex.implementation.language.flow.LanguageChangeListener;
import ru.ancap.framework.artifex.implementation.language.input.LAPIInitialLanguageInstaller;
import ru.ancap.framework.artifex.implementation.language.input.LanguageInput;
import ru.ancap.framework.artifex.implementation.language.module.BaseLanguageController;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.artifex.implementation.scheduler.SchedulerAPILoader;
import ru.ancap.framework.artifex.implementation.scheduler.SchedulerSilencer;
import ru.ancap.framework.artifex.implementation.timer.EveryDayTask;
import ru.ancap.framework.artifex.implementation.timer.TimerExecutor;
import ru.ancap.framework.artifex.implementation.timer.heartbeat.ArtifexHeartbeat;
import ru.ancap.framework.artifex.status.tests.*;
import ru.ancap.framework.command.api.commands.exception.exception.UnawaitedRawCommandException;
import ru.ancap.framework.command.api.commands.exception.exception.UnknownSubCommandException;
import ru.ancap.framework.command.api.commands.exception.exception.UnpermittedActionException;
import ru.ancap.framework.command.api.commands.flag.exception.FlagDataNotProvidedException;
import ru.ancap.framework.command.api.commands.flag.exception.NotAFlagException;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.database.sql.SQLDatabase;
import ru.ancap.framework.database.sql.connection.reader.DatabaseFromConfig;
import ru.ancap.framework.database.sql.registry.Repository;
import ru.ancap.framework.database.sql.registry.RegistryInitialization;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.locale.BasicLocales;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.PluginLoadTask;
import ru.ancap.framework.status.test.Test;
import ru.ancap.framework.util.AudienceProvider;
import ru.ancap.framework.util.player.StepbackMaster;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@ToString
@Accessors(fluent = true)
public final class Artifex extends AncapPlugin implements Listener {

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
            "language", new LanguageInput(ArtifexConfig.loaded().nativeLanguage()), 
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
    private LAPIInitialLanguageInstaller languageInstaller;

    @Override
    public void onCoreLoad() {
        this.loadBukkitToKyori();
        this.loadCommonMessageDomains();
        this.loadTPSCounter();
        this.loadPlayerLeaveInstructor();
        this.loadStepbackMaster();
        this.loadAncap();
        this.loadConfiguration();
        this.loadIdentifiers();
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
    
    private void loadIdentifiers() {
        Identifier.CONSOLE_ID = ArtifexConfig.loaded().section().getString("console.id");
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
            new CommandAPITest(this.commandRegistrar()),
            new LAPITest(this, this.languageInstaller),
            new ConfigurationTest(this),
            new MainListenerAutoregisterTest(this)
        );
    }

    private void loadClickableMessageActionProxy() {
        new ActionProxy("cmap").setup(this.commandRegistrar());
    }

    private void loadEventAPI() {
        this.eventApiListeners.forEach(this::registerEventsListener);
    }
    
    private static int[] getCurrentVersion() {
        String[] minecraftVersionString = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        int[] minecraftVersion = new int[minecraftVersionString.length];
        for (int i = 0; i < minecraftVersionString.length; i++) {
            minecraftVersion[i] = Integer.parseInt(minecraftVersionString[i]);
        }
        return minecraftVersion;
    }

    private void loadCommonMessageDomains() {
        ArtifexCommonMessageDomains.init();
    }

    private void loadInstance() {
        AncapPlugin.CORE_IMPL = this;
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
        this.asyncCommandCenter = new AsyncCommandCenter(this.configuration().getBoolean("verbose-generic-error-message"), AncapPlugin.proxy);
        this.registerCommandCenter(this.asyncCommandCenter);
        this.registerCommandExceptionCenter(this.asyncCommandCenter);
        this.registerDefaultExceptionOperators();
        this.ancap.installGlobalCommandOperator(this, this.asyncCommandCenter, this.asyncCommandCenter);
    }
    
    private void registerDefaultExceptionOperators() {
        this.commandExceptionCenter().register(NoNextArgumentException.class,              new NoNextArgumentExceptionOperator());
        this.commandExceptionCenter().register(UnknownSubCommandException.class,           new NoSubCommandExceptionOperator());
        this.commandExceptionCenter().register(UnawaitedRawCommandException.class,         new UnawaitedRawCommandExceptionOperator());
        this.commandExceptionCenter().register(UnpermittedActionException.class,           new UnpermittedActionExceptionOperator());
        this.commandExceptionCenter().register(FlagDataNotProvidedException.class,         new FlagDataNotProvidedExceptionOperator());
        this.commandExceptionCenter().register(NotAFlagException.class,                    new NotAFlagExceptionOperator());
    }
    
    private void loadAncap() {
        AncapBukkit.CORE_PLUGIN = this;
        this.ancap = new ArtifexAncap(this.tpsCounter, this.stepbackMaster, this.debugIndicatorFile());
        this.ancap.load();
        this.loadAncap(this.ancap);
    }

    @SneakyThrows
    private File debugIndicatorFile() {
        File folder = new File(this.getDataFolder().getParentFile().getParentFile(), "debug");
        if (!folder.exists()) Files.createDirectories(folder.toPath());
        return new File(folder, "debug.indicator");
    }

    @SneakyThrows
    private void loadSchedulerAPI() {
        SQLDatabase schedulerDatabase = new DatabaseFromConfig(
            this,
            ArtifexConfig.loaded().section().getConfigurationSection("database.scheduler-database")
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
            ArtifexConfig.loaded().section().getConfigurationSection("database.main-database")
        ).load();
    }

    private void loadConfiguration() {
        new ArtifexConfig(this.configuration("configuration.yml")).load();
    }

    @SneakyThrows
    private void loadLAPI() {
        Repository<String, SpeakerModel, SpeakerModel> speakerRepository = new RegistryInitialization<>(this.database, SpeakerModel.class).run();
        this.languageInstaller = LAPIInitialLanguageInstaller.initialize(speakerRepository, this);
        LAPI.setup(
            new BasicLocales(
                ArtifexConfig.loaded().targetFallbackMap(),
                ArtifexConfig.loaded().defaultFallback(),
                ArtifexConfig.loaded().nativeLanguage()
            ), 
            new BaseLanguageController(speakerRepository)
        );
    }

    private void startHeartbeat() {
        ArtifexHeartbeat heartbeat = new ArtifexHeartbeat(this);
        heartbeat.start();
    }
    
    public static volatile boolean mainListenerAutoregisterTestEventAccepted = false;
    
    @EventHandler
    public void on(MainListenerAutoregisterTestEvent event) {
        mainListenerAutoregisterTestEventAccepted = true;
    }
    
    public static class MainListenerAutoregisterTestEvent extends Event {
        public static final HandlerList handlers = new HandlerList();
        public static HandlerList getHandlerList() {return handlers;}
        @NotNull @Override public HandlerList getHandlers() {return handlers;}
    }
    
}