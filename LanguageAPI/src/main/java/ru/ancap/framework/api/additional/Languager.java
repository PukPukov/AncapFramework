package ru.ancap.framework.api.additional;

import ru.ancap.framework.api.language.Language;

import java.util.Arrays;
import java.util.List;

public interface Languager {

    String localized(String id);
    void setupLanguage(Language language);

    default List<String> localizedList(String id) {
        return Arrays.stream(localized(id).split("\n")).toList();
    }

}
