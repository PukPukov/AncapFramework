package ru.ancap.framework.artifex.implementation.command.object;

import org.bukkit.entity.Player;
import ru.ancap.framework.command.api.commands.object.dispatched.InlineTextCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;

public class PacketCommandWrite extends CommandWrite {

    public PacketCommandWrite(int transactionID, InlineTextCommand packetData, Player player, InlineTextCommand provideCommand) {
        super(
                new PacketLineSpeaker(
                        transactionID,
                        packetData,
                        player
                ),
                provideCommand
        );
    }
}
