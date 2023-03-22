package ru.ancap.framework.language.additional;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.communicate.replacement.Replacement;
import ru.ancap.framework.language.LAPI;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class LAPIMessage extends WrapperMessage implements CallableMessage {
    
    public LAPIMessage(String id, Replacement... replacements) {
        super(nameIdentifier -> new Message(LAPI.localized(id, nameIdentifier), replacements).call(nameIdentifier));
    }

    public LAPIMessage(Class<?> class_, String id, Replacement... replacements) {
        this(LAPIDomain.of(class_, id), replacements);
    }
    
}
