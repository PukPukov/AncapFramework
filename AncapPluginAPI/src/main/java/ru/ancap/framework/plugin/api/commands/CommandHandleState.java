package ru.ancap.framework.plugin.api.commands;

import lombok.With;
import lombok.experimental.WithBy;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;

@With @WithBy
public record CommandHandleState(CommandOperator operator, @Nullable AncapPlugin owner) {}
