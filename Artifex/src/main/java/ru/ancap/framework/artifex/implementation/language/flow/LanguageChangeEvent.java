package ru.ancap.framework.artifex.implementation.language.flow;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.language.language.Language;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true) @Getter
public class LanguageChangeEvent extends Event {

    private final CommandSender sender;
    private final Language language;

    public LanguageChangeEvent(CommandSender sender, String languageCode) {
        this.sender = sender;
        this.language = Language.of(languageCode);
    }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}

}