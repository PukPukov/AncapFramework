package ru.ancap.framework.artifex.implementation.command.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ProxiedCommandEvent extends Event implements Cancellable {

    private final CommandSender sender;
    private final String command;
    
    @Getter @Setter private boolean cancelled = false;
    
    public CommandSender sender() { return this.sender; }
    public String command() { return this.command; }

    private static final HandlerList handlers = new HandlerList();
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
    
}
