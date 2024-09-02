package ru.ancap.framework.plugin.api;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NonBlocking;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AncapBukkit {
    
    public static JavaPlugin CORE_PLUGIN;
    
    @NonBlocking
    public static void sendCommand(CommandSender sender, String command) {
        Bukkit.getScheduler().runTask(
            CORE_PLUGIN,
            () -> {
                Bukkit.dispatchCommand(sender, command);
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
        @NonNull String id,
        @NonNull JavaPlugin owner,
        @NonNull List<String> sources,
        @NonNull CommandExecutor executor
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
        @NonNull String commandName
    ) {
        SimpleCommandMap map = (SimpleCommandMap) FieldUtils.readField(Bukkit.getServer(), "commandMap", true);
        @SuppressWarnings("unchecked")
        Map<String, Command> knownCommands = (Map<String, Command>) FieldUtils.readField(map, "knownCommands", true);
        for (String alias : Bukkit.getPluginCommand(commandName).getAliases()) knownCommands.remove(alias);
        knownCommands.remove(commandName);
        syncCommands();
    }

    @SneakyThrows
    public static void syncCommands() {
        Class<?> server = Class.forName(PaperShitWorkarounds.craftBukkitPackage()+".CraftServer");
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