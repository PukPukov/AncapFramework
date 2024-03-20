package ru.ancap.framework.artifex.implementation.command.center;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.*;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.plugin.api.AncapFrameworkAPI;
import ru.ancap.framework.runtime.Artifex;
import ru.ancap.framework.artifex.implementation.command.event.ProxiedCommandEvent;
import ru.ancap.framework.artifex.implementation.command.object.PacketCommandWrite;
import ru.ancap.framework.artifex.implementation.command.object.SenderSource;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.InlineTextCommand;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.TextCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.additional.LAPIMessage;

import java.util.*;
import java.util.function.Consumer;

@Getter(AccessLevel.PRIVATE)
@ToString @EqualsAndHashCode
public class CommandCatcher implements Listener, PacketListener {
    
    private final AncapFrameworkAPI ancap;

    @Delegate
    private final PacketListener delegate;
    private final CSCommandOperator global;
    private final OperateRule rule;
    
    private final Map<String, Long> lastRequestTimeMap = new HashMap<>();
    
    private boolean versionCacheSetup;
    private boolean versionCache;
    
    private final Consumer<PacketEvent> onTabComplete = (event) -> {
        if (event.isCancelled()) return;
        if (this.getAncap().getServerTPS() < 19.5) {
            String identifier = Identifier.of(event.getPlayer());
            long currentTime = System.currentTimeMillis();
            Long lastTime = this.lastRequestTimeMap.get(identifier);
            if (lastTime != null && currentTime - lastTime < 500) return;
            this.lastRequestTimeMap.put(identifier, currentTime);
        }
        PacketContainer packet = event.getPacket();
        String text = packet.getStrings().read(0);
        int transactionID = this.readTransactionId() ? packet.getIntegers().read(0) : 0;
        InlineTextCommand inlineTextCommand = new InlineTextCommand(text);
        if (!this.getRule().isOperate(inlineTextCommand)) return;
        event.setCancelled(true);
        this.getGlobal().on(new PacketCommandWrite(
            transactionID,
            inlineTextCommand,
            event.getPlayer(),
            inlineTextCommand
        ));
    };

    private boolean readTransactionId() {
        if (!this.versionCacheSetup) {
            this.versionCache = ProtocolLibrary.getProtocolManager().getMinecraftVersion().compareTo(new MinecraftVersion("1.13")) >= 0;
            this.versionCacheSetup = true;
        }
        return this.versionCache;
    }

    public CommandCatcher(AncapFrameworkAPI ancap, JavaPlugin plugin, CSCommandOperator global, OperateRule rule) {
        this.ancap = ancap;
        var onTabComplete = this.onTabComplete;
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
        this.operateInterceptableDispatch(
            Interceptable.ofEvent(event),
            new SenderSource(event.getPlayer()),
            event.getMessage(),
            this::operateInterceptableDispatch
        );
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
    public void on(ServerCommandEvent event) {
        this.operateInterceptableDispatch(
            Interceptable.ofEvent(event),
            new SenderSource(event.getSender()),
            event.getCommand(),
            this::operateInterceptableDispatch
        );
    }
    
    @EventHandler
    public void on(ProxiedCommandEvent event) {
        this.operateInterceptableDispatch(
            Interceptable.ofEvent(event),
            new SenderSource(event.sender()),
            event.command(),
            this::operateInterceptableDispatch
        );
    }

    private String commandLineWithoutSlash(String commandLine) {
        if (commandLine.startsWith("/")) commandLine = commandLine.substring(1);
        return commandLine;
    }

    private void notifyAboutServerCommand(CommandSender sender, String command) {
        Communicator.of(Bukkit.getConsoleSender()).message(new LAPIMessage(
            Artifex.class, "command.api.info.issued-server-command",
            new Placeholder("source", sender.getName()),
            new Placeholder("command", command)
        ));
    }

    private void operateInterceptableDispatch(InterceptableCommandForm form) {
        this.global.on(new CommandDispatch(form.source, form.command));
        form.interceptable.intercept();
    }

    private void operateInterceptableDispatch(Interceptable interceptable, CommandSource source, String sent, Consumer<InterceptableCommandForm> consumer) {
        if (interceptable.intercepted()) return;
        sent = this.commandLineWithoutSlash(sent);
        TextCommand command = this.from(source, sent);
        if (!this.rule.isOperate(command)) return;
        consumer.accept(new InterceptableCommandForm(interceptable, source, command));
    }

    private TextCommand from(CommandSource source, String message) {
        return this.from(new RawForm(source, message));
    }

    private TextCommand from(RawForm form) {
        return this.from(new Form(form.source, new ArrayList<>(Arrays.asList(form.command.split(" ")))));
    }

    private TextCommand from(Form form) {
        return new TextCommand(form.arguments);
    }

    private record RawForm(CommandSource source, String command) { }
    private record Form(CommandSource source, List<String> arguments) { }
    private record InterceptableCommandForm(Interceptable interceptable, CommandSource source, CSCommand command) { }
    
    public interface Interceptable {
        
        static Interceptable ofEvent(Cancellable event) {
            
            return new Interceptable() {
                @Override
                public boolean intercepted() {
                    return event.isCancelled();
                }

                @Override
                public void intercept() {
                    event.setCancelled(true);
                }
            };
            
        }
        
        boolean intercepted();
        void intercept();
        
    }

}