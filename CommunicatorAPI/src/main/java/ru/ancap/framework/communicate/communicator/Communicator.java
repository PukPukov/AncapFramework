package ru.ancap.framework.communicate.communicator;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Blocking;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.ArrayList;
import java.util.List;

public interface Communicator {
    
    static Communicator of(CommandSender sender) {
        return BaseCommunicator.of(sender);
    }
    
    static Communicator of(CommandSender... senders) {
        List<Communicator> communicators = new ArrayList<>();
        for (CommandSender sender : senders) communicators.add(Communicator.of(sender));
        return new MultiCommunicator(communicators); // No streams because.. beca.. ah.. im gonna.. optimizoooooooom
    }

    @Blocking
    void message(CallableMessage callable);
    
    /* when deminecraftized
    Audience platform(); 
    Component textOf(CallableMessage message); 
    */
    
}
