package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.commons.cache.CacheMap;

/**
 * @deprecated in fact, this cache is not optimization but pessimization; with contexted calls,
 * planned to introduce in 1.7, everything will be even worse. Will be removed in 1.7.
 */
@AllArgsConstructor
@ToString @EqualsAndHashCode
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.7")
public class CacheMessage implements CallableMessage {
    
    private final CallableMessage delegate;
    private final CacheMap<String, String> cache = new CacheMap<>();

    @Override
    public String call(String identifier) {
        return this.cache.get(identifier, () -> this.delegate.call(identifier));
    }
    
}
