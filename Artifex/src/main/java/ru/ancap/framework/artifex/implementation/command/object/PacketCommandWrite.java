package ru.ancap.framework.artifex.implementation.command.object;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.entity.Player;
import ru.ancap.framework.command.api.commands.object.dispatched.TextCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class PacketCommandWrite extends CommandWrite {
    
    public PacketCommandWrite(int transactionID, TextCommand inlineCommand, Player player) {
        super(
            new PacketLineSpeaker(
                transactionID,
                inlineCommand,
                player
            ),
            inlineCommand
        );
    }
}