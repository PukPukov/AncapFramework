package ru.ancap.framework.plugin.api.commands;

import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;

import java.util.List;

public interface CommandCenterDataPatch {

    /**
     * Used to revert patch when unloading plugin. If not set, will be unlinked, where possible, and linked to AncapFramework Runtime, where isn't possible.
     */
    CommandCenterDataPatch link(JavaPlugin plugin);
    CommandCenterDataPatch handler(CommandEventHandler handler);
    CommandCenterDataPatch aliases(List<String> aliases);
    CommandCenterDataPatch addAliases(List<String> aliases);
    
    void exec();
    
    default CommandCenterDataPatch csoperator(CSCommandOperator operator) {
        return this.handler(new CSAPIEventHandler(operator));
    }
    
    
}
