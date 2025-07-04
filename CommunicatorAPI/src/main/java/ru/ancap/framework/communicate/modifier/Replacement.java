package ru.ancap.framework.communicate.modifier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.Text;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Replacement implements Modifier {
    
    private final String from;
    private final @NonNull CallableText to;
    
    public Replacement(String base, Object replacement) {
        this(base, new Text(replacement+""));
    }

    @Override
    public String apply(String base, String identifier) {
        return base.replace(this.from, this.to.call(identifier));
    }
    
}