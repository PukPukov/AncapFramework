package ru.ancap.framework.communicate.communicator.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ru.ancap.commons.cache.CacheMap;

          // CachedMiniMessageSerializer
public class CMMSerializer {

    private static final CacheMap<String, Component> serializeCache = new CacheMap<>();

    public static Component serialize(String string) {
        return serializeCache.get(string, () -> MiniMessage.miniMessage().deserialize(string));
    }
    
}
