package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Since CallableMessage should support toString(), equals() and hashCode() providing lambda to this class constructor is discouraged
 */
@AllArgsConstructor
public class WrapperMessage implements CallableMessage {
    
    @Delegate
    private final CallableMessage delegate;
    
}
