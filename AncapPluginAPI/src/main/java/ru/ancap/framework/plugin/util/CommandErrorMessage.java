package ru.ancap.framework.plugin.util;

import lombok.experimental.Delegate;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

public class CommandErrorMessage implements CallableMessage {
    
    @Delegate
    private final CallableMessage message;
    
    public CommandErrorMessage(CallableMessage inner) {
        this.message = new LAPIMessage(
            CommonMessageDomains.Error.operateIsImpossible,
            new Placeholder("description", inner)
        );
    }
    
    public static void send(CommandSender sender, CallableMessage inner) {
        Communicator.of(sender).message(new CommandErrorMessage(inner));
        if (sender instanceof Player player) player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2, 0);
    }
    
}