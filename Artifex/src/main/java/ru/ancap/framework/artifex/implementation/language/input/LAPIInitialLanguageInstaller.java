package ru.ancap.framework.artifex.implementation.language.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.database.sql.registry.Repository;
import ru.ancap.framework.identifier.Identifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LAPIInitialLanguageInstaller implements Listener {

    private final Repository<String, SpeakerModel, SpeakerModel> speakerRepository;
    private final ExecutorService thread = Executors.newSingleThreadExecutor();
    
    public static LAPIInitialLanguageInstaller initialize(Repository<String, SpeakerModel, SpeakerModel> speakerRepository, JavaPlugin plugin) {
        var installer = new LAPIInitialLanguageInstaller(speakerRepository);
        Bukkit.getPluginManager().registerEvents(installer, plugin);
        installer.loadConsoleLanguage();
        return installer;
    }
    
    private void loadConsoleLanguage() {
        this.prepareSpeaker(
            ArtifexConfig.loaded().section().getString("console.id"),
            () -> ArtifexConfig.loaded().section().getString("console.language")
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerJoinEvent event) {
        this.thread.execute(() -> prepareSpeaker(Identifier.of(event.getPlayer()), () -> localeFromMinecraftFormat(event.getPlayer().getLocale())));
    }
    
    public void prepareSpeaker(String id, Supplier<String> localeSupplier) {
        if (this.speakerRepository.read(id).isPresent()) return;
        this.speakerRepository.save(id, new SpeakerModel(id, localeSupplier.get()));
    }

    public static String localeFromMinecraftFormat(String minecraftFormat) {
        return minecraftFormat.split("_")[0];
    }
    
}