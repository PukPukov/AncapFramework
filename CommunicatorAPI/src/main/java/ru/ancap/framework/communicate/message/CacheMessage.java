package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.commons.cache.CacheMap;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class CacheMessage implements CallableMessage {
    
    private final CallableMessage delegate;
    private final CacheMap<String, String> cache = new CacheMap<>();

    @Override
    public String call(String nameIdentifier) {
        return this.cache.get(nameIdentifier, () -> this.delegate.call(nameIdentifier));
    }
    
}
