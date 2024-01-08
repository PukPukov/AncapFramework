package ru.ancap.framework.plugin.api.commands;

import org.bukkit.command.CommandSender;

public record BukkitCommandDispatch(CommandSender sender, String line) {
}
