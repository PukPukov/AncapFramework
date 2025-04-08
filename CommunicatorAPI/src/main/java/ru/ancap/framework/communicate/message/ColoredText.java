package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ColoredText implements CallableText {
    
    private final CallableText base;
    private final CallableText color;

    @Override
    public String call(String identifier) {
        String base = this.base.call(identifier);
        String color = this.color.call(identifier);
        return "<"+color+">"+base+"</"+color+">";
    }
    
}