package ru.ancap.framework.artifex.implementation.command.center;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.artifex.implementation.command.object.PacketCommandWrite;
import ru.ancap.framework.artifex.implementation.command.object.SenderSource;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.InlineTextCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.TextCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.plugin.api.Ancap;

import java.util.*;
import java.util.function.Consumer;

@Getter(value = AccessLevel.PRIVATE)
public class CommandCatcher implements Listener, PacketListener {
    
    private final Ancap ancap;

    @Delegate
    private final PacketListener delegate;
    private final CommandOperator global;
    private final OperateRule rule;
    
    private final Map<String, Long> lastRequestTimeMap = new HashMap<>();
    
    private boolean versionCacheSetup;
    private boolean versionCache;
    
    private final Consumer<PacketEvent> onTabComplete = (event) -> {
        if (event.isCancelled()) {
            return;
        }
        if (this.getAncap().getServerTPS() < 19.5) {
            String playerName = event.getPlayer().getName();
            long currentTime = System.currentTimeMillis();
            Long lastTime = this.lastRequestTimeMap.get(playerName);
            if (lastTime != null && currentTime - lastTime < 500) return;
            this.lastRequestTimeMap.put(playerName, currentTime);
        }
        PacketContainer packet = event.getPacket();
        String text = packet.getStrings().read(0);
        int transactionID = this.readTransactionId() ? packet.getIntegers().read(0) : 0;
        InlineTextCommand inlineTextCommand = new InlineTextCommand(text);
        if (!this.getRule().isOperate(inlineTextCommand)) {
            return;
        }
        event.setCancelled(true);
        this.getGlobal().on(
                new PacketCommandWrite(
                        transactionID,
                        inlineTextCommand,
                        event.getPlayer(),
                        inlineTextCommand
                )
        );
    };

    private boolean readTransactionId() {
        if (!this.versionCacheSetup) {
            this.versionCache = ProtocolLibrary.getProtocolManager().getMinecraftVersion().compareTo(new MinecraftVersion("1.13")) >= 0;
            this.versionCacheSetup = true;
        }
        return this.versionCache;
    }

    public CommandCatcher(Ancap ancap, JavaPlugin plugin, CommandOperator global, OperateRule rule) {
        this.ancap = ancap;
        this.delegate = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                onTabComplete.accept(event);
            }
        };
        this.global = global;
        this.rule = rule;
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        this.operateDispatch(
                event,
                new SenderSource(event.getPlayer()), event.getMessage().substring(1),
                this :: operateDispatch
        );
    }

    @EventHandler
    public void on(ServerCommandEvent event) {
        this.operateDispatch(
                event,
                new SenderSource(event.getSender()), event.getCommand(),
                this :: operateDispatch
        );
    }

    private void operateDispatch(EventCommandForm form) {
        this.global.on(
                new CommandDispatch(
                        form.source,
                        form.command
                )
        );
        form.event.setCancelled(true);
    }

    private void operateDispatch(Cancellable event, CommandSource source, String sent, Consumer<EventCommandForm> consumer) {
        if (event.isCancelled()) return;
        TextCommand command = this.from(source, sent);
        if (!this.rule.isOperate(command)) return;
        consumer.accept(new EventCommandForm(event, source, command));
    }

    private TextCommand from(CommandSource source, String message) {
        return this.from(
                new RawForm(source, message)
        );
    }

    private TextCommand from(RawForm form) {
        return this.from(new Form(
                form.source,
                new ArrayList<>(Arrays.asList(form.command.split(" ")))
        ));
    }

    private TextCommand from(Form form) {
        return new TextCommand(
                form.arguments
        );
    }

    @AllArgsConstructor
    private static class RawForm {
        private final CommandSource source;
        private final String command;
    }

    @AllArgsConstructor
    private static class Form {
        private final CommandSource source;
        private final List<String> arguments;
    }

    @AllArgsConstructor
    private static class EventCommandForm {
        private final Cancellable event;
        private final CommandSource source;
        private final LeveledCommand command;
    }

}
