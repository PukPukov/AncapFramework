package ru.ancap.framework.plugin.event.events.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.event.CommandEvent;
import ru.ancap.framework.api.language.Language;


@Getter
public class LanguageChangeEvent extends CommandEvent {

    private final Language language;

    public LanguageChangeEvent(CommandSender sender, String languageCode) {
        super(sender);
        this.language = Language.of(languageCode);
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}