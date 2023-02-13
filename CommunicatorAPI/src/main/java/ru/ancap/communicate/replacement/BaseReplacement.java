package ru.ancap.communicate.replacement;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import ru.ancap.communicate.message.CallableMessage;
import ru.ancap.communicate.message.Message;

@AllArgsConstructor
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
