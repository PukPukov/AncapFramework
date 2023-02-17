package ru.ancap.framework.resource.config;

import org.jetbrains.annotations.Nullable;

public interface TransferMap {
    
    @Nullable String getTarget(String old);
    
}
