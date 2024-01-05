package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ColoredMessage implements CallableMessage {
    
    private final CallableMessage base;
    private final CallableMessage color;

    @Override
    public String call(String receiverId) {
        String base = this.base.call(receiverId);
        String color = this.color.call(receiverId);
        return "<"+color+">"+base+"</"+color+">";
    }
    
}
