package ru.ancap.framework.language.additional;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.language.LAPI;

@ToString(callSuper = true) @EqualsAndHashCode
public class LAPIMessage implements CallableMessage {

    private final String key;
    private final Modifier[] modifiers;

    public LAPIMessage(String key, Modifier... modifiers) {
        this.key = key;
        this.modifiers = modifiers;
    }

    public LAPIMessage(Class<?> class_, String key, Modifier... modifiers) {
        this(LAPIDomain.of(class_, key), modifiers);
    }

    @Override
    public String call(String receiverId) {
        return new Message(LAPI.localized(this.key, receiverId)).call(receiverId);
    }

}
