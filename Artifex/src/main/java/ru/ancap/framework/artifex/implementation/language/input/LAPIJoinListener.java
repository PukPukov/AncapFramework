package ru.ancap.framework.artifex.implementation.language.input;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;
import ru.ancap.framework.identifier.Identifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class LAPIJoinListener implements Listener {

    private final SpeakerModelRepository repository;
    
    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerJoinEvent event) {
        this.thread.execute(() -> {
            if (this.repository.read(Identifier.of(event.getPlayer())) != null) return;
            this.repository.create(new SpeakerModel(Identifier.of(event.getPlayer()), localeFromMinecraftFormat(event.getPlayer().getLocale())));
        });
    }

    public static String localeFromMinecraftFormat(String minecraftFormat) {
        return minecraftFormat.split("_")[0];
    }
    
}