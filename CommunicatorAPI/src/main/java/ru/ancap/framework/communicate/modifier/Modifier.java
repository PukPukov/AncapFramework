package ru.ancap.framework.communicate.modifier;

import java.util.function.BiFunction;

public interface Modifier extends BiFunction<String, String, String> {
    
    String apply(String base, String identifier);

}
