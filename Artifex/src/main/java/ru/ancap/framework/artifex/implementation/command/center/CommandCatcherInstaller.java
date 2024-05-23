package ru.ancap.framework.artifex.implementation.command.center;

import com.comphenix.protocol.ProtocolLibrary;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import ru.ancap.framework.artifex.implementation.PacketLibFork;
import ru.ancap.framework.artifex.implementation.command.center.util.catcher.tab.CommonTabCatcher;
import ru.ancap.framework.artifex.implementation.command.center.util.catcher.tab.PacketEventsTabCatcher;
import ru.ancap.framework.artifex.implementation.command.center.util.catcher.tab.ProtocolLibTabCatcher;
import ru.ancap.framework.artifex.implementation.command.center.util.TabPerformanceChecker;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.plugin.api.AncapPlugin;

@UtilityClass
public class CommandCatcherInstaller {

    public static void install(AncapPlugin owner, CommandOperator global, OperateRule scope) {
        TabPerformanceChecker tabPerformanceChecker = new TabPerformanceChecker(() -> (float) owner.ancap().getServerTPS());
        
        DispatchCatcher catcher = new DispatchCatcher(owner.ancap(), global, scope);
        Bukkit.getPluginManager().registerEvents(catcher, owner);
        
        var commonTabCatcher = new CommonTabCatcher(tabPerformanceChecker, global, scope);
        
        switch (PacketLibFork.CurrentUsage.TAB_COMPLETE_RECEIVE.packetLib()) {
            case ProtocolLib -> {
                var protocolLibTabCatcher = new ProtocolLibTabCatcher(owner, commonTabCatcher);
                ProtocolLibrary.getProtocolManager().addPacketListener(protocolLibTabCatcher);
            }
            case PacketEvents -> {
                var packetEventsTabCatcher = new PacketEventsTabCatcher(commonTabCatcher);
                PacketEvents.getAPI().getEventManager().registerListener(packetEventsTabCatcher, PacketListenerPriority.LOW);
            }
        }
    }

}