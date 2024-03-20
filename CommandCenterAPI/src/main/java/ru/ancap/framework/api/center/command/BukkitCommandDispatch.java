package ru.ancap.framework.api.center.command;

import org.bukkit.command.CommandSender;

public record BukkitCommandDispatch(CommandSender sender, String line) { }