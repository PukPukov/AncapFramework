package ru.ancap.framework.communicate.message;

public class AtNextLine extends WrapperText {
    
    public AtNextLine(CallableText message) {
        super(new MultilineText(
            Text.EMPTY,
            message
        ));
    }
    
}