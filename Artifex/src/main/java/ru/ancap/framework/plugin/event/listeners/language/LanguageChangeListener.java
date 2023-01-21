package ru.ancap.framework.plugin.event.listeners.language;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.framework.api.additional.LAPISpeaker;
import ru.ancap.framework.api.additional.Languager;
import ru.ancap.framework.api.language.Language;
import ru.ancap.framework.plugin.Artifex;
import ru.ancap.framework.plugin.event.events.command.LanguageChangeEvent;

public class LanguageChangeListener implements Listener {

    @EventHandler
    public void on(LanguageChangeEvent event) {
        Language language = event.getLanguage();
        Languager languager = new LAPISpeaker(event.getSender().getName());
        languager.setupLanguage(language);
        String message = languager.localized(
                Artifex.MESSAGE_DOMAIN+"language.change"
        );
        String placeholder = languager.localized(
                Artifex.MESSAGE_DOMAIN+"languages."+language.getCode()
        );
        message = message.replace("%LANG%", placeholder);
        event.getSender().sendMessage(message);
    }

}
