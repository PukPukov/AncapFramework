package ru.ancap.framework.communicate.modifier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;
import ru.ancap.framework.communicate.message.CallableText;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Placeholder implements Modifier {

    @Delegate
    private final Modifier delegate;

    public Placeholder(String base, CallableText callableText) {
        this(new Replacement(Placeholder.placeholderFromBase(base), callableText));
    }
    
    public Placeholder(String base, Object object) {
        this(new Replacement(Placeholder.placeholderFromBase(base), object));
    }

    public static String placeholderFromBase(String base) {
        if (!base.startsWith("%")) base = "%"+base;
        if (!base.endsWith("%")) base = base+"%";
        base = base.toUpperCase();
        base = base.replaceAll("[\\s-]" /* spaces and dashes*/ , "_");
        return base;
    }
    
}