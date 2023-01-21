package ru.ancap.api.units.number;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface LanguagePreset {

    @Nullable
    String caseFor(int lastDigit);

}
