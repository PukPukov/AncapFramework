package ru.ancap.framework.artifex.implementation.command.center.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class EscapingBuffer {
    
    private boolean currentlyEscaped;
    private boolean escapeNext;
    
    public void step() {
        this.currentlyEscaped = this.escapeNext;
        this.escapeNext = false;
    }
    
    public void escapeNext() {
        this.escapeNext = true;
    }
    
}