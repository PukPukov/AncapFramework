package ru.ancap.framework.communicate.modifier;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ModifierBundle implements Modifier {
    
    private final List<Modifier> modifiers;

    @Override
    public String apply(String base, String receiverId) {
        for (Modifier modifier : this.modifiers) {
            base = modifier.apply(base, receiverId);
        }
        return base;
    }
    
}
