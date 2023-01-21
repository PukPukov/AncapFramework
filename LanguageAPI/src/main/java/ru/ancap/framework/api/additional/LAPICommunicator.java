package ru.ancap.framework.api.additional;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.LAPI;
import ru.ancap.util.Replacement;
import ru.ancap.util.communicate.Communicator;

@RequiredArgsConstructor
public class LAPICommunicator {
    
    private final CommandSender sender;
    private final Communicator communicator;
    
    public LAPICommunicator(CommandSender sender) {
        this(sender, new Communicator(sender));
    }

    public void send(String id, Replacement... replacements) {
        communicator.send(LAPI.localized(id, sender.getName()), replacements);
    }
}
