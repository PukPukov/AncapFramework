package ru.ancap.framework.resource;

import java.io.File;
import java.io.InputStream;

public interface ResourcePreparator<T> {
    
    T prepare(InputStream base, File file);
    
}
