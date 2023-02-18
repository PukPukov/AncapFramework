package ru.ancap.framework.configuration;

public class Case {

    public static String camelToKebab(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }
    
}
