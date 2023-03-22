package ru.ancap.framework.communicate.replacement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class BaseReplacement implements Replacement {
    
    private final String base;
    
    @Delegate
    private final CallableMessage callableMessage;
    
    public BaseReplacement(String base, Object replacement) {
        this(base, new Message(replacement+""));
    }
    
    public String base() {
        return this.base;
    }
    
    public CallableMessage message() {
        return this.callableMessage;
    }
    
}
