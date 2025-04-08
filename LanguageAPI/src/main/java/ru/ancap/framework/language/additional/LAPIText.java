package ru.ancap.framework.language.additional;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.message.CacheText;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.language.LAPI;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class LAPIText extends CacheText implements CallableText {
    
    public LAPIText(String id, Modifier... modifiers) {
        super(nameIdentifier -> new Text(LAPI.localized(id, nameIdentifier), modifiers).call(nameIdentifier));
    }

    public LAPIText(Class<?> class_, String id, Modifier... modifiers) {
        this(LAPIDomain.of(class_, id), modifiers);
    }
    
}