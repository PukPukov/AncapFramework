package ru.ancap.framework.language.additional;

public class LAPIDomain {
    
    public static String of(Class<?> class_, String sub) {
        return class_.getPackageName() + "." + sub;
    }
    
}