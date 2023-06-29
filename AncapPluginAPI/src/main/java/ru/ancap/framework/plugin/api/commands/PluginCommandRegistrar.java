package ru.ancap.framework.plugin.api.commands;

import lombok.AllArgsConstructor;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;

import java.util.List;

@AllArgsConstructor
public class PluginCommandRegistrar {
    
    private final AncapPlugin plugin;
    private final CommandCenter global;
    
    public void register(String commandName, CommandOperator operator) {
        this.register(commandName, List.of(), operator);
    }
    
    public void register(String commandName, List<String> aliases, CommandOperator operator) {
        this.global.register(commandName, new CommandHandleState(commandName, aliases, this.plugin, operator));
    }
    
    public void unregister(String commandName) {
        this.global.unregister(commandName);
    }

    public void setExecutor(String commandName, CommandOperator operator) {
        this.global.setExecutor(commandName, operator);
    }
    
    public void establishExecutor(String commandName, CommandOperator operator) {
        if (this.global.findRegisterStateOf(commandName) != null) this.setExecutor(commandName, operator);
        else this.register(commandName, operator);
    }
    
}
