package ru.ancap.framework.artifex.implementation.command.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.util.AudienceProvider;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString @EqualsAndHashCode
public class SenderSource implements CommandSource {
    
    private final CommandSender sender;
    private final Audience audience;
    
    public SenderSource(CommandSender sender) {
        this(sender, AudienceProvider.bukkitAudiences().sender(sender));
    }
    
    @Override
    public String identifier() {
        return Identifier.of(this.sender);
    }
    
    @Override
    public Communicator communicator() {
        return Communicator.of(this.sender);
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