package ru.ancap.framework.plugin.command.api.implementation.catcher;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.github.retrooper.packetevents.event.CancellableEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.api.command.commands.command.dispatched.InlineTextCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.TextCommand;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.delegate.operate.OperateRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter(value = AccessLevel.PRIVATE)
public class CommandCatcher implements Listener, PacketListener {

    @Delegate
    private final PacketListener delegate;
    private final CommandOperator global;
    private final OperateRule rule;
    
    private final Consumer<PacketEvent> onTabComplete = (event) -> {
        if (event.isCancelled()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        String text = packet.getStrings().read(0);
        int transactionID = packet.getIntegers().read(0);
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
    
    public CommandCatcher(JavaPlugin plugin, CommandOperator global, OperateRule rule) {
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
                event.getPlayer(), event.getMessage().substring(1),
                this :: operateDispatch
        );
    }

    @EventHandler
    public void on(ServerCommandEvent event) {
        this.operateDispatch(
                event,
                event.getSender(), event.getCommand(),
                this :: operateDispatch
        );
    }

    private void operateDispatch(EventCommandForm form) {
        this.global.on(
                new CommandDispatch(
                        form.sender,
                        form.command
                )
        );
        form.event.setCancelled(true);
    }

    private void operateDispatch(Cancellable event, CommandSender sender, String sent, Consumer<EventCommandForm> consumer) {
        if (event.isCancelled()) return;
        TextCommand command = this.from(sender, sent);
        if (!this.rule.isOperate(command)) return;
        consumer.accept(new EventCommandForm(event, sender, command));
    }

    private TextCommand from(CommandSender sender, String message) {
        return this.from(
                new RawForm(sender, message)
        );
    }

    private TextCommand from(RawForm form) {
        return this.from(new Form(
                form.sender,
                new ArrayList<>(Arrays.asList(form.command.split(" ")))
        ));
    }

    private TextCommand from(Form form) {
        return new TextCommand(
                form.arguments
        );
    }

    @AllArgsConstructor
    @Data
    private static class RawForm {
        private final CommandSender sender;
        private final String command;
    }

    @AllArgsConstructor
    @Data
    private static class Form {
        private final CommandSender sender;
        private final List<String> arguments;
    }

    @AllArgsConstructor
    @Data
    private static class EventCommandForm {
        private final Cancellable event;
        private final CommandSender sender;
        private final LeveledCommand command;
    }

    @AllArgsConstructor
    @Data
    private static class CancellableAbstraction implements Cancellable {
        @Delegate
        private final CancellableEvent event;
    }

}
