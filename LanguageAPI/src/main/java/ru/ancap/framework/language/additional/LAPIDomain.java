package ru.ancap.framework.language.additional;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LAPIDomain {
    
    public static String of(Class<?> class_, String... subs) {
        return class_.getPackageName() + "." + String.join(".", subs);
    }
    
}