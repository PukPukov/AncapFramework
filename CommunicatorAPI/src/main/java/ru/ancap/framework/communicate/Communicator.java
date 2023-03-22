package ru.ancap.framework.communicate;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Blocking;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.util.AudienceProvider;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class Communicator {
    
    private final Audience audience;
    private final String nameIdentifier;
    
    public Communicator(CommandSender sender) {
        this(Communicator.audienceOf(sender), sender.getName());
    }

    private static Audience audienceOf(CommandSender sender) {
        return sender instanceof Player ? AudienceProvider.bukkitAudiences().player((Player) sender) : AudienceProvider.bukkitAudiences().sender(sender);
    }
    
    public Audience audience() {
        return this.audience;
    }
    
    public String nameIdentifier() {
        return this.nameIdentifier;
    }

    @Blocking
    public void send(CallableMessage callable) {
        this.audience.sendMessage(MiniMessage.miniMessage().deserialize(callable.call(this.nameIdentifier)));
    }
    
}
