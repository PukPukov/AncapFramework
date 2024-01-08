package ru.ancap.framework.plugin.api.commands;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;

public interface BukkitCommandLineSpeaker {

    CommandSender sender();
    void sendTab(TabBundle tab);
    
}
