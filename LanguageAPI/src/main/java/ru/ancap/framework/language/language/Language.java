package ru.ancap.framework.language.language;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Language {

    private static Map<String, Language> map;
    
    private final String code;

    private Language(String code) {
        this.code = code;
    }

    public static Language of(@NotNull String languageCode) {
        if (Language.map == null) Language.map = new HashMap<>();
        if (Language.map.get(languageCode) == null) {
            Language language = new Language(languageCode);
            Language.map.put(languageCode, language);
            return language;
        }
        return Language.map.get(languageCode);
    }
    
    public String code() {
        return this.code;
    }
}
