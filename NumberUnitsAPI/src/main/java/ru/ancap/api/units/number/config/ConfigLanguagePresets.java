package ru.ancap.api.units.number.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.api.units.number.LanguagePreset;
import ru.ancap.api.units.number.LanguagePresets;

@AllArgsConstructor
public class ConfigLanguagePresets implements LanguagePresets {

    private final ConfigurationSection section;

    @Override
    public LanguagePreset get(String languageCode) {
        ConfigurationSection presetSection = this.section.getConfigurationSection(languageCode);
        return lastDigit -> {
            for (String key : presetSection.getKeys(false)) {
                for (Integer integer : presetSection.getIntegerList(key)) {
                    if (integer == lastDigit) {
                        return key;
                    }
                }
            }
            return null;
        };
    }
}
