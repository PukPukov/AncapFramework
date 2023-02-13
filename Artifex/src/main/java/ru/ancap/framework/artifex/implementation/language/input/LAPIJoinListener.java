package ru.ancap.framework.artifex.implementation.language.input;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;

@AllArgsConstructor
public class LAPIJoinListener implements Listener {

    private final SpeakerModelRepository repository;

    @EventHandler
    public void on(PlayerJoinEvent event) {
        if (repository.read(event.getPlayer().getName()) != null) return;
        repository.create(
                new SpeakerModel(
                        event.getPlayer().getName(),
                        ArtifexConfig.loaded().defaultLanguage().code()
                )
        );
    }
}
