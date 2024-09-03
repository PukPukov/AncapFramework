package ru.ancap.framework.communicate.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ru.ancap.framework.communicate.communicator.util.CMMSerializer;
import ru.ancap.framework.identifier.Identifier;

public interface CallableMessage {

    /**
     * @return String in minimessage format.
     */
    String call(String identifier);
    
    default Component component(Player player) {
        return this.component(Identifier.of(player));
    }
    
    default Component component(String identifier) {
        return CMMSerializer.serialize(this.call(identifier));
    }
    
}