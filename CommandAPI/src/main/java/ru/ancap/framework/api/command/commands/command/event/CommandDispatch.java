package ru.ancap.framework.api.command.commands.command.event;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;

public record CommandDispatch(
        CommandSender sender, 
        LeveledCommand dispatched
) {}
