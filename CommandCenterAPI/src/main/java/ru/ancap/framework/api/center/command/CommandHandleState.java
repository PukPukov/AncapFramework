package ru.ancap.framework.api.center.command;

import lombok.With;
import lombok.experimental.WithBy;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

@With @WithBy
public record CommandHandleState(CommandEventHandler handler, @Nullable JavaPlugin owner) { }