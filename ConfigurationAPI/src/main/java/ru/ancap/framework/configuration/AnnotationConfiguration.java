package ru.ancap.framework.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.commons.TriFunction;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class AnnotationConfiguration {
    
    @Getter(value = AccessLevel.PRIVATE)
    private static final Map<Class<?>, TriFunction<Field, ConfigurationSection, String, ?>> specialExtractors = Map.of(
            List.class, (field, master, path) -> {
                Class<?> type = field.getAnnotation(GenericType.class).value();
                List<String> list = master.getStringList(path);
                List<Object> returnList = new ArrayList<>();
                Function<String, ?> converter = AnnotationConfiguration.getConverters().get(type);
                list.forEach((string) -> returnList.add(converter.apply(string)));
                return returnList;
            },
            Map.class, (field, master, path) -> {
                ConfigurationSection mapSection = master.getConfigurationSection(path);
                Map<String, Object> map = new HashMap<>();
                Set<String> keys = mapSection.getKeys(false);
                Class<?> type = field.getAnnotation(GenericType.class).value();
                for (String key : keys) {
                    map.put(key, AnnotationConfiguration.getConverters().get(type).apply(mapSection.getString(key)));
                }
                return map;
            },
            ConfigurationSection.class, (field, master, path) -> master.getConfigurationSection(path)
    );
    
    @Getter(value = AccessLevel.PRIVATE)
    private static final Map<Class<?>, Function<String, ?>> converters = Map.of(
            Material.class, (string) -> Material.getMaterial(string.toUpperCase()),
            Long.class, Long::valueOf,
            Boolean.class, Boolean::valueOf,
            Double.class, Double::valueOf,
            String.class, string -> string
    );
    
    @SneakyThrows
    public static void load(Class<?> config, ConfigurationSection section) {
        Field[] fields = config.getDeclaredFields();
        for (Field field : fields) {
            Configure configure = field.getAnnotation(Configure.class);
            Class<?> type = field.getType();
            String path = (configure == null || configure.value().equals("")) ? field.getName() : configure.value();
            path = Case.camelToKebab(path);
            field.setAccessible(true);
            Object value;
            var extractor = AnnotationConfiguration.getSpecialExtractors().get(type);
            if (extractor != null) value = extractor.apply(field, section, path);
            else value = AnnotationConfiguration.getConverters().get(type).apply(section.getString(path));
            field.set(null, value);
        }
    }

}
