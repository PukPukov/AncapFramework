package ru.ancap.framework.communicate.communicator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import ru.ancap.commons.cache.CacheMap;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.util.CMMSerializer;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.util.AudienceProvider;

@ToString @EqualsAndHashCode
@Accessors(fluent = true) @Getter
@RequiredArgsConstructor
public class BaseCommunicator implements Communicator {

    private final String identifier;
    @EqualsAndHashCode.Exclude
    private final Audience audience;
    
    // PRIVATE CONSTRUCTORS
    private BaseCommunicator(CommandSender sender) {
        this(Identifier.of(sender), BaseCommunicator.audienceOf(sender));
    }

    private static Audience audienceOf(CommandSender sender) {
        return AudienceProvider.bukkitAudiences().sender(sender);
    }

    // CACHE
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private static final CacheMap<CommandSender, BaseCommunicator> cache = new CacheMap<>();
    public static BaseCommunicator of(CommandSender sender) {
        return BaseCommunicator.cache.get(sender, () -> new BaseCommunicator(sender));
    }

    // ACTUAL MESSAGING
    @Override
    public void message(CallableText message) {
        this.audience.sendMessage(CMMSerializer.serialize(message.call(this.identifier())));
    }

}