package ru.ancap.api.units.number.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.api.units.number.LanguagePresets;

@AllArgsConstructor
public class NumberUnitsConfigReader implements NumberUnitsProvider {

    private final LanguagePresets presets;
    private final ConfigurationSection section;

    @Override
    public String atNumber(String word, int lastDigit) {
        String languageCode = section.getString("preset");
        String langCase = presets.get(languageCode).caseFor(lastDigit);
        return section.getString(word+"."+langCase);
    }
}
