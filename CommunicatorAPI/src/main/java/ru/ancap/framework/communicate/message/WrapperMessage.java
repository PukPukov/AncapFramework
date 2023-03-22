package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class WrapperMessage implements CallableMessage {
    
    @Delegate
    private final CallableMessage delegate;
    
}
