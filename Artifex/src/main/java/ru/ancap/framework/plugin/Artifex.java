package ru.ancap.framework.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.database.ConnectionProvider;
import ru.ancap.framework.api.locale.MapLocales;
import ru.ancap.framework.api.plugin.plugins.AncapBukkit;
import ru.ancap.framework.api.plugin.plugins.AncapPlugin;
import ru.ancap.framework.api.reader.ConfigDatabaseLoader;
import ru.ancap.framework.api.reader.ConfigDriverSettingsReader;
import ru.ancap.framework.plugin.command.api.implementation.AsyncCommandCenter;
import ru.ancap.framework.plugin.command.api.implementation.catcher.CommandCatcher;
import ru.ancap.framework.plugin.command.library.commands.LanguageCommandExecutor;
import ru.ancap.framework.plugin.configuration.ArtifexConfig;
import ru.ancap.framework.plugin.event.listeners.api.addition.BlockClickListener;
import ru.ancap.framework.plugin.event.listeners.api.addition.VillagerHealListener;
import ru.ancap.framework.plugin.event.listeners.api.wrapper.ExplodeListener;
import ru.ancap.framework.plugin.event.listeners.api.wrapper.ProtectListener;
import ru.ancap.framework.plugin.event.listeners.api.wrapper.SelfDestructListener;
import ru.ancap.framework.plugin.event.listeners.command.CommandEventsListener;
import ru.ancap.framework.plugin.event.listeners.language.LanguageChangeListener;
import ru.ancap.framework.plugin.event.timer.ArtifexTimerEventListener;
import ru.ancap.framework.plugin.heartbeat.ArtifexHeartbeat;
import ru.ancap.framework.plugin.language.listener.LAPIJoinListener;
import ru.ancap.framework.plugin.language.module.LanguageBase;
import ru.ancap.framework.plugin.language.module.LanguagesOperator;
import ru.ancap.framework.plugin.language.module.repository.SQLSpeakerModelRepository;
import ru.ancap.framework.plugin.language.module.repository.SpeakerModelRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class Artifex extends AncapPlugin {

    public static PluginDescriptionFile INFO;
    public static final String MESSAGE_DOMAIN = "ru.ancap.framework.messages.";

    private final List<Listener> listeners = List.of(
            new ProtectListener(),
            new SelfDestructListener(),
            new ExplodeListener(),
            new VillagerHealListener(),
            new BlockClickListener(),
            new ArtifexTimerEventListener(),
            new LanguageChangeListener()
    );
    
    private final Map<String, CommandOperator> commands = Map.of(
            "language", new LanguageCommandExecutor()
    );
    
    private CommandCatcher commandCatcher;

    private AsyncCommandCenter asyncCommandCenter;
    private ConnectionProvider connectionProvider;

    @Override
    public void onCoreLoad() {
        this.loadConfig();
        this.loadAncap();
        this.registerCommandCenter();
        this.startHeartbeat();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.loadCommandCatcher();
        this.registerIntegrators();
        this.loadListeners();
        this.loadDatabase();
        this.loadLAPI();
        this.loadLocales();
    }

    private void loadCommandCatcher() {
        this.commandCatcher = new CommandCatcher(this, asyncCommandCenter, asyncCommandCenter);
    }

    private void loadListeners() {
        this.registerEventsListener(this.commandCatcher);
        this.registerEventsListener(
                new CommandEventsListener(
                        this.getAncap().getTypeNameProvider()
                )
        );
        ProtocolLibrary.getProtocolManager().addPacketListener(commandCatcher);
    }

    private void registerCommandCenter() {
        this.asyncCommandCenter = new AsyncCommandCenter();
        this.registerCommandCenter(
                asyncCommandCenter
        );
    }

    private void loadAncap() {
        AncapBukkit.CORE_PLUGIN = this;
        this.getLogger().info("Loading ANCAP");
        this.loadAncap(
                new ArtifexAncap(
                        new ConfigDriverSettingsReader(
                                ArtifexConfig.loaded().getDatabaseDriverDataSection()
                        ).get(),
                        new LAPITypeNameProvider()
                )
        );
        this.getLogger().info("ANCAP loaded as "+this.getAncap().toString());
    }

    private void loadDatabase() {
        this.connectionProvider = new ConfigDatabaseLoader(
                this,
                this.getAncap().getGlobalDatabaseProperties(),
                ArtifexConfig.loaded().getDatabaseConnectionSection(),
                context -> {
                    String sql = """
                                 CREATE TABLE IF NOT EXISTS Languages (
                                 name VARCHAR(32) PRIMARY KEY,
                                 language_code VARCHAR(8) 
                                 );
                                 """;
                    try (PreparedStatement statement = context.connection().prepareStatement(sql)) {
                        statement.execute();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).load().provider();
    }

    private void loadConfig() {
        new ArtifexConfig(
                this.getConfig()
        ).load();
    }

    private void loadLAPI() {
        SpeakerModelRepository repository = new SQLSpeakerModelRepository(this.connectionProvider);
        this.registerEventsListener(
                new LAPIJoinListener(repository)
        );
        LAPI.setup(
                new MapLocales(
                        ArtifexConfig.loaded().defaultLanguage()
                ),
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

    @Override
    public List<Listener> listeners() {
        return this.listeners;
    }

    @Override
    public Map<String, CommandOperator> commands() {
        return this.commands;
    }
}
