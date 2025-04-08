package ru.ancap.framework.artifex.implementation.language.flow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.language.language.Language;

public class LanguageChangeListener implements Listener {

    @EventHandler
    public void on(LanguageChangeEvent event) {
        Language language = event.language();
        LAPI.updateLanguage(Identifier.of(event.sender()), language);
        Communicator.of(event.sender()).message(new LAPIText(Artifex.class, "command.language.setup"));
    }

}