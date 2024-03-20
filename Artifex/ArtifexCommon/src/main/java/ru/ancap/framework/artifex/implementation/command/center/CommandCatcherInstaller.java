package ru.ancap.framework.artifex.implementation.command.center;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;

@UtilityClass
public class CommandCatcherInstaller {

    public static void install(CommandCatcher catcher, AncapPlugin owner) {
        Bukkit.getPluginManager().registerEvents(catcher, owner);
        ProtocolLibrary.getProtocolManager().addPacketListener(catcher);
    }

}
