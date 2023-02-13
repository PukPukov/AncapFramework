package ru.ancap.framework.artifex.implementation.language.module;

public interface LanguagesData {

    String languageCode(String speakerId, String defaultCode);
    void setPlayerLanguage(String speakerId, String languageCode);

}
