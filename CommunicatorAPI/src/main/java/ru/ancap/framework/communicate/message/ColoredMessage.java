package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ColoredMessage implements CallableMessage {
    
    private final CallableMessage base;
    private final CallableMessage color;

    @Override
    public String call(String identifier) {
        String base = this.base.call(identifier);
        String color = this.color.call(identifier);
        return "<#"+color+">"+base+"</#"+color+">";
    }
    
}