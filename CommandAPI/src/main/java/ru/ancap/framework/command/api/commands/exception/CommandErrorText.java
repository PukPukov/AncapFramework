package ru.ancap.framework.command.api.commands.exception;

import lombok.experimental.Delegate;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

public class CommandErrorText implements CallableText {
    
    @Delegate
    private final CallableText message;
    
    public CommandErrorText(CallableText inner) {
        this.message = new LAPIText(
            CommonMessageDomains.Error.operateIsImpossible,
            new Placeholder("description", inner)
        );
    }
    
    public static void send(CommandSender sender, CallableText inner) {
        Communicator.of(sender).message(new CommandErrorText(inner));
        if (sender instanceof Player player) player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 2, 0);
    }
    
}