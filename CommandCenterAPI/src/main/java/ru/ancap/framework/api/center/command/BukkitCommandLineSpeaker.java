package ru.ancap.framework.api.center.command;

import org.bukkit.command.CommandSender;

public interface BukkitCommandLineSpeaker {
    
    CommandSender sender();
    void tab(TabBundle tab);
    
}