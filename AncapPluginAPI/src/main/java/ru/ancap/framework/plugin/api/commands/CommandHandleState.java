package ru.ancap.framework.plugin.api.commands;

import lombok.With;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;

import java.util.List;

@With
public record CommandHandleState(String commandName, List<String> aliases, AncapPlugin owner, CommandOperator operator) {}
