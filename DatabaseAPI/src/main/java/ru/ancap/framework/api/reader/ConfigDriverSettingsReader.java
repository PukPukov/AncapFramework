package ru.ancap.framework.api.reader;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.framework.api.database.DatabaseConnectionProperty;
import ru.ancap.framework.api.reader.DatabaseType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@AllArgsConstructor
public class ConfigDriverSettingsReader implements Supplier<Map<String, DatabaseConnectionProperty>> {

    private final ConfigurationSection section;

    @Override
    public Map<String, DatabaseConnectionProperty> get() {
        Map<String, DatabaseConnectionProperty> map = new HashMap<>();
        for (String key : section.getKeys(false)) {
            map.put(
                    key,
                    new DatabaseConnectionProperty(
                            section.getString(key + ".driver.domain"),
                            section.getString(key + ".driver.class"),
                            DatabaseType.valueOf(
                                    section.getString(key+".type").toUpperCase()
                            )
                    )
            );
        }
        return map;
    }
}
