package ru.ancap.framework.artifex.implementation.language.input;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.database.sql.registry.Registry;
import ru.ancap.framework.identifier.Identifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class LAPIInitialLanguageInstaller implements Listener {

    private final Registry<String, SpeakerModel, SpeakerModel> speakerRegistry;
    
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

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