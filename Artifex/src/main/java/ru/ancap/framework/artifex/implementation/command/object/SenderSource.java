package ru.ancap.framework.artifex.implementation.command.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.util.AudienceProvider;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class SenderSource implements CommandSource {
    
    private final CommandSender sender;
    private final Audience audience;
    
    public SenderSource(CommandSender sender) {
        this(sender, AudienceProvider.bukkitAudiences().sender(sender));
    }

    @Override
    public CommandSender sender() {
        return this.sender;
    }

    @Override
    public Audience audience() {
        return this.audience;
    }
}
