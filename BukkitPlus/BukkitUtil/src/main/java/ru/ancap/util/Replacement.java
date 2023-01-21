package ru.ancap.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Replacement {
    
    private final String base;
    private final String replacement;
    
    public Replacement(String base, Object replacement) {
        this(base, replacement+"");
    }
}
