package ru.ancap.framework.artifex.implementation.language.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.database.sql.registry.Registry;
import ru.ancap.framework.identifier.Identifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LAPIInitialLanguageInstaller implements Listener {

    private final Registry<String, SpeakerModel, SpeakerModel> speakerRegistry;
    private final ExecutorService thread = Executors.newSingleThreadExecutor();
    
    public static LAPIInitialLanguageInstaller initialize(Registry<String, SpeakerModel, SpeakerModel> speakerRegistry, JavaPlugin plugin) {
        var installer = new LAPIInitialLanguageInstaller(speakerRegistry);
        Bukkit.getPluginManager().registerEvents(installer, plugin);
        return installer;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerJoinEvent event) {
        this.thread.execute(() -> {
            if (this.speakerRegistry.read(Identifier.of(event.getPlayer())).isPresent()) return;
            String id = Identifier.of(event.getPlayer());
            this.speakerRegistry.save(
                id,
                new SpeakerModel(id, localeFromMinecraftFormat(event.getPlayer().getLocale()))
            );
        });
    }

    public static String localeFromMinecraftFormat(String minecraftFormat) {
        return minecraftFormat.split("_")[0];
    }
    
}