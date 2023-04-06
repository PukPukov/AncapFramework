package ru.ancap.framework.artifex.implementation.command.center;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.artifex.implementation.command.event.ProxiedCommandEvent;

public class CommandProxy implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {
        String line = command.getName() + " " + String.join(" ", arguments);
        Bukkit.getPluginManager().callEvent(new ProxiedCommandEvent(sender, line));
        return true;
    }
    
}
