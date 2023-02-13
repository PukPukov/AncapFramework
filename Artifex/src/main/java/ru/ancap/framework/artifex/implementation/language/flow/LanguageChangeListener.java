package ru.ancap.framework.artifex.implementation.language.flow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.communicate.Communicator;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.language.language.Language;

public class LanguageChangeListener implements Listener {

    @EventHandler
    public void on(LanguageChangeEvent event) {
        Language language = event.language();
        LAPI.setupLanguage(event.sender().getName(), language);
        new Communicator(event.sender()).send(new LAPIMessage(
                Artifex.class, "command.language.setup"
        ));
    }

}
