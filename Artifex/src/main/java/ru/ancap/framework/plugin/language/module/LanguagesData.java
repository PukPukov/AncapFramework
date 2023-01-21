package ru.ancap.framework.plugin.language.module;

public interface LanguagesData {

    String languageCode(String speakerId, String defaultCode);
    void setPlayerLanguage(String speakerId, String languageCode);

}
