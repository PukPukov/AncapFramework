package ru.ancap.framework.artifex.implementation.language.flow;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.command.api.event.CommandEvent;
import ru.ancap.framework.language.language.Language;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class LanguageChangeEvent extends CommandEvent {

    private final Language language;

    public LanguageChangeEvent(CommandSender sender, String languageCode) {
        super(sender);
        this.language = Language.of(languageCode);
    }
    
    public Language language() {
        return this.language;
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}