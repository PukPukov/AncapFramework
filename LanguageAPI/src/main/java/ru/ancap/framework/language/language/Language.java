package ru.ancap.framework.language.language;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

@ToString @EqualsAndHashCode
@ApiStatus.Internal // since 1.7
public class Language {

    private static Map<String, Language> map;
    
    private final String code;

    private Language(String code) {
        this.code = code;
    }

    public static Language of(@NonNull String languageCode) {
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
