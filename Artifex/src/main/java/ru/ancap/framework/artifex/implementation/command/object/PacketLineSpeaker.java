package ru.ancap.framework.artifex.implementation.command.object;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.apiguardian.api.API;
import org.bukkit.entity.Player;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.dispatched.Part;
import ru.ancap.framework.command.api.commands.object.dispatched.TextCommand;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;

import java.util.List;

@ToString @EqualsAndHashCode
public class PacketLineSpeaker implements CommandLineSpeaker {
    
    public static final int MAX_TAB_COMPLETIONS = 25;
    
    @API(status = API.Status.STABLE)
    public static void sendTabPacket(Player player, int transactionID, TextCommand command, TabBundle tab) {
        int start;
        int end;
        var hotPart = command.hotPart();
        if (hotPart.isPresent()) {
            start = hotPart.get().beginIndexInclusive()+1;
            end = hotPart.get().endIndexInclusive()+2;
        } else {
            start = command.parts().getLast().endIndexInclusive()+3;
            end = start+1;
        }
        WrapperPlayServerTabComplete.CommandRange commandRange = new WrapperPlayServerTabComplete.CommandRange(
            start,
            end
        );
        
        List<WrapperPlayServerTabComplete.CommandMatch> commandMatches = tab.tabCompletions().stream()
            .map(ancapCompletion -> new WrapperPlayServerTabComplete.CommandMatch(
                ancapCompletion.completion(),
                ancapCompletion.tooltipState().orElse(null)
            ))
            .toList();
        
        WrapperPlayServerTabComplete packet = new WrapperPlayServerTabComplete(transactionID, commandRange, commandMatches);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
    
    private final int transactionID;
    private final Player player;
    private final TextCommand command;
    
    public PacketLineSpeaker(int transactionID, TextCommand command, Player player) {
        this.transactionID = transactionID;
        this.player = player;
        this.command = command;
    }
    
    @Override
    public int transactionId() {
        return this.transactionID;
    }
    
    @Override
    public void sendTab(@NonNull TabBundle tab) {
        var tabCompletionsStream = tab.tabCompletions().stream();
        if (tab.filter()) tabCompletionsStream = tabCompletionsStream.filter(s -> s.completion().startsWith(this.command.hotPart().map(Part::main).orElse("")));
        tabCompletionsStream = tabCompletionsStream.limit(MAX_TAB_COMPLETIONS);
        
        PacketLineSpeaker.sendTabPacket(this.player, this.transactionID, this.command, tab.withTabCompletions(tabCompletionsStream.toList()));
    }
    
}