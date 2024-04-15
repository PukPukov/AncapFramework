package ru.ancap.framework.database.sql.registry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString @EqualsAndHashCode(callSuper = true)
public class AmbiguousSelfConvertableConversionAttemptException extends RuntimeException {
    
    private final String initialId;
    private final String ambiguousId;
    private final Class<?> class_;
    
    @Override
    public String getMessage() {
        return "Ambiguous conversion, attempted to convert self-convertable "+this.class_.getName()+" with id "+this.initialId+" to containing id "+this.ambiguousId;
    }
    
}