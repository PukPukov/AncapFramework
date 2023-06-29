package ru.ancap.framework.communicate.communicator;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Blocking;
import ru.ancap.commons.cache.CacheMap;
import ru.ancap.framework.communicate.communicator.util.CMMSerializer;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.util.AudienceProvider;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class BaseCommunicator implements Communicator {

    @EqualsAndHashCode.Exclude
    private final Audience audience;
    private final String nameIdentifier;
    
    private BaseCommunicator(CommandSender sender) {
        this(BaseCommunicator.audienceOf(sender), Identifier.of(sender));
    }

    private static Audience audienceOf(CommandSender sender) {
        return AudienceProvider.bukkitAudiences().sender(sender);
    }

    @ToString.Exclude @EqualsAndHashCode.Exclude
    private static final CacheMap<CommandSender, BaseCommunicator> cache = new CacheMap<>();
    public static BaseCommunicator of(CommandSender sender) {
        return BaseCommunicator.cache.get(sender, () -> new BaseCommunicator(sender));
    }

    public Audience audience() { return this.audience; }
    public String nameIdentifier() { return this.nameIdentifier; }

    @Blocking
    @Override public void message(CallableMessage message) {
        this.audience.sendMessage(this.component(message));
    }
    
    public Component component(CallableMessage callable) {
        String called = callable.call(this.nameIdentifier);
        return CMMSerializer.serialize(called);
    }

}
