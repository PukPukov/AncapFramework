package ru.ancap.framework.plugin.api.commands;

import lombok.RequiredArgsConstructor;
import ru.ancap.commons.list.merge.MergeList;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;

import java.util.List;

@RequiredArgsConstructor
public class PluginCommandRegistrar {
    
    private final AncapPlugin plugin;
    private final CommandCenter global;
    
    public void register(String commandName, CommandOperator operator) {
        this.register(commandName, List.of(), operator);
    }
    
    public void register(String commandName, List<String> aliases, CommandOperator operator) {
        this.global.register(commandName, new MergeList<>(List.of(commandName), aliases), new CommandHandleState(operator, this.plugin));
    }
    
    public void unregister(String commandName) {
        this.global.unregister(commandName);
    }

    public void setExecutor(String commandName, CommandOperator operator) {
        this.global.setExecutor(commandName, operator);
    }
    
}
