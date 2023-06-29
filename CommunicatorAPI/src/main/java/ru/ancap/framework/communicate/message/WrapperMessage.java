package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class WrapperMessage implements CallableMessage {
    
    @Delegate
    private final CallableMessage delegate;
    
}
