package ru.ancap.framework.communicate.message;

public class AtNextLine extends WrapperMessage {
    
    public AtNextLine(CallableMessage message) {
        super(new MultilineMessage(
            Message.EMPTY,
            message
        ));
    }
    
}
