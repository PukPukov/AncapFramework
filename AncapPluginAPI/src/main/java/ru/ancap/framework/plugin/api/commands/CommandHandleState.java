package ru.ancap.framework.plugin.api.commands;

import lombok.With;
import lombok.experimental.WithBy;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@With @WithBy
public record CommandHandleState(CommandEventHandler handler, @Nullable JavaPlugin owner) {}
