package ru.ancap.communicate.replacement;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import ru.ancap.communicate.message.CallableMessage;

@AllArgsConstructor
public class Placeholder implements Replacement {

    @Delegate
    private final Replacement delegate;

    public Placeholder(String base, CallableMessage callableMessage) {
        this(new BaseReplacement(Placeholder.placeholderFromBase(base), callableMessage));
    }
    
    public Placeholder(String base, Object object) {
        this(new BaseReplacement(Placeholder.placeholderFromBase(base), object));
    }

    private static String placeholderFromBase(String base) {
        if (!base.startsWith("%")) base = "%"+base;
        if (!base.endsWith("%")) base = base+"%";
        base = base.toUpperCase();
        base = base.replaceAll("[\\s-]" /* spaces and dashes*/ , "_");
        return base;
    }
}

