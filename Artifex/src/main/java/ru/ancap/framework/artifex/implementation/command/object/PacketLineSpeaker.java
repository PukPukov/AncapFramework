package ru.ancap.framework.artifex.implementation.command.object;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.InlineTextCommand;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;

import java.util.stream.Collectors;

@ToString @EqualsAndHashCode
public class PacketLineSpeaker implements CommandLineSpeaker {

    private final int transactionID;
    
    private final CommandSource source;
    
    private final Player player;
    
    private final InlineTextCommand command;

    public PacketLineSpeaker(int transactionID, InlineTextCommand command, Player player) {
        this.transactionID = transactionID;
        this.player = player;
        this.source = new SenderSource(player);
        this.command = command;
    }

    @Override
    public void sendTab(@NonNull TabBundle tab) {
        if (tab.filter()) tab = tab.withTabCompletions(tab.tabCompletions().stream()
                .filter(s -> s.completion().startsWith(this.command.getHotArgument()))
                .collect(Collectors.toList())
        );

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
        
        if (protocolManager.getMinecraftVersion().compareTo(new MinecraftVersion("1.13")) >= 0) {
            packet.getIntegers().write(0, this.transactionID);

            StringRange range = new StringRange(
                    this.command.argumentStart(tab.argumentsToReplace())+1,
                    this.command.argumentsEnd()+1
            );

            Suggestions suggestions = new Suggestions(range, tab.tabCompletions().stream()
                    .map(completion -> new Suggestion(
                            range,
                            completion.completion(),
                            completion.tooltipState().map(component -> new LiteralMessage(
                                    PlainTextComponentSerializer.plainText().serialize(component)
                            )).orElse(null)
                    )).collect(Collectors.toList())
            );
            
            packet.getSpecificModifier(Suggestions.class).write(0, suggestions);
        } else {
            String[] completions = new String[tab.tabCompletions().size()];
            
            for (int i = 0; i < tab.tabCompletions().size(); i++) completions[i] = tab.tabCompletions().get(i).completion();
            packet.getStringArrays().write(0, completions);
        }
        
        protocolManager.sendServerPacket(this.player, packet);
    }

    @Override
    public CommandSource source() {
        return this.source;
    }
    
}
