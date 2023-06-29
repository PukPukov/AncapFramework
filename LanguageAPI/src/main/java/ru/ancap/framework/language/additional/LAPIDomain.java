package ru.ancap.framework.language.additional;

public class LAPIDomain {
    
    public static String of(Class<?> class_, String... subs) {
        return class_.getPackageName() + "." + String.join(".", subs);
    }
    
}