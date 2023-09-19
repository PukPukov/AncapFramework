package ru.ancap.framework.plugin.api;

import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import ru.ancap.commons.debug.AncapDebug;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AncapBukkit {
    
    public static JavaPlugin CORE_PLUGIN;
    
    @NonBlocking
    public static void sendCommand(CommandSender sender, String command) {
        Bukkit.getScheduler().callSyncMethod(
            CORE_PLUGIN,
            () -> {
                Bukkit.dispatchCommand(sender, command);
                return Void.TYPE;
            }
        );
    }
    
    @NonBlocking
    public static void sendConsoleCommand(String command) {
        AncapBukkit.sendCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * @param id id of command. Use primary command name as it
     * @param sources command names, that player will type after "/" to enter command. Includes primary command name and aliases
     */

    @SneakyThrows
    public static void registerCommandExecutor(
        @NotNull String id,
        @NotNull JavaPlugin owner,
        @NotNull List<String> sources,
        @NotNull CommandExecutor executor
    ) {
        CommandMap map = (CommandMap) FieldUtils.readField(Bukkit.getServer(), "commandMap", true);
        Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);
        PluginCommand command = constructor.newInstance(id, owner);
        command.setAliases(sources);
        command.setExecutor(executor);
        map.register(owner.getName(), command);
        syncCommands();
        Bukkit.getPluginCommand(id).setExecutor(executor);
    }

    @SneakyThrows
    public static void unregisterCommandExecutor(
        @NotNull String commandName
    ) {
        SimpleCommandMap map = (SimpleCommandMap) FieldUtils.readField(Bukkit.getServer(), "commandMap", true);
        AncapDebug.debugArray(FieldUtils.getAllFields(SimpleCommandMap.class));
        @SuppressWarnings("unchecked")
        Map<String, Command> knownCommands = (Map<String, Command>) FieldUtils.readField(map, "knownCommands", true);
        for (String alias : Bukkit.getPluginCommand(commandName).getAliases()) knownCommands.remove(alias);
        knownCommands.remove(commandName);
        syncCommands();
    }

    @SneakyThrows
    private static void syncCommands() {
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        Class<?> server = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
        Method syncCommands = server.getDeclaredMethod("syncCommands");
        syncCommands.setAccessible(true);
        syncCommands.invoke(Bukkit.getServer());
    }

    public static class CommandTypeInject extends Command implements PluginIdentifiableCommand {
        
        protected JavaPlugin plugin;
        protected final CommandExecutor owner;
        protected final Object registeredWith;

        public CommandTypeInject(String[] commands, String desc, String usage, CommandExecutor owner, Object registeredWith,
                                 JavaPlugin plugin) {
            super(commands[0], desc, usage, Arrays.asList(commands));
            this.owner = owner;
            this.plugin = plugin;
            this.registeredWith = registeredWith;
        }

        @Override
        public JavaPlugin getPlugin() {
            return this.plugin;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            if (!this.testPermission(sender)) return true;
            if (this.owner.onCommand(sender, this, label, args)) return true;
            else {
                sender.sendMessage(this.usageMessage);
                return false;
            }
        }
        
    }
    
}
