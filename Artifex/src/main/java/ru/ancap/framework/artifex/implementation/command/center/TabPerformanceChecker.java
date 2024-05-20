package ru.ancap.framework.artifex.implementation.command.center;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@ToString @EqualsAndHashCode
@RequiredArgsConstructor
public class TabPerformanceChecker {
    
    private final Supplier<Float> serverTPSProvider;
    private final Map<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();
    
    public boolean skipFor(Supplier<String> identifierSupplier) {
        if (serverTPSProvider.get() < 19.5) {
            String identifier = identifierSupplier.get();
            long currentTime = System.currentTimeMillis();
            Long lastTime = this.lastRequestTimeMap.get(identifier);
            if (lastTime != null && currentTime - lastTime < 500) return true;
            this.lastRequestTimeMap.put(identifier, currentTime);
            return false;
        } else return false;
    }
    
}