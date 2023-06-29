package ru.ancap.framework.language.additional;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.message.CacheMessage;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.language.LAPI;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class LAPIMessage extends CacheMessage implements CallableMessage {
    
    public LAPIMessage(String id, Modifier... modifiers) {
        super(nameIdentifier -> new Message(LAPI.localized(id, nameIdentifier), modifiers).call(nameIdentifier));
    }

    public LAPIMessage(Class<?> class_, String id, Modifier... modifiers) {
        this(LAPIDomain.of(class_, id), modifiers);
    }
    
}
