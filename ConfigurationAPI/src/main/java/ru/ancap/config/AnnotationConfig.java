package ru.ancap.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.commons.TriFunction;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class AnnotationConfig {
    
    @Getter(value = AccessLevel.PRIVATE)
    private static final Map<Class<?>, TriFunction<Field, ConfigurationSection, String, ?>> specialExtractors = Map.of(
            List.class, (field, section, path) -> {
                Class<?> type = field.getAnnotation(GenericType.class).value();
                List<String> list = section.getStringList(path);
                List<Object> returnList = new ArrayList<>();
                Function<String, ?> converter = AnnotationConfig.getConverters().get(type);
                list.forEach((string) -> returnList.add(converter.apply(string)));
                return returnList;
            },
            Map.class, (field, section, path) -> {
                Map<String, Object> map = new HashMap<>();
                Set<String> keys = section.getKeys(false);
                Class<?> type = field.getAnnotation(GenericType.class).value();
                for (String key : keys) {
                    map.put(key, AnnotationConfig.getConverters().get(type).apply(key));
                }
                return map;
            },
            ConfigurationSection.class, (field, section, path) -> section.getConfigurationSection(path)
    );
    
    @Getter(value = AccessLevel.PRIVATE)
    private static final Map<Class<?>, Function<String, ?>> converters = Map.of(
            Material.class, (string) -> Material.getMaterial(string.toUpperCase()),
            Long.class, Long::valueOf,
            Boolean.class, Boolean::valueOf,
            Double.class, Double::valueOf
            
    );
    
    @SneakyThrows
    public static void load(Class<?> config, ConfigurationSection section) {
        Field[] fields = config.getDeclaredFields();
        for (Field field : fields) {
            Configure configure = field.getAnnotation(Configure.class);
            if (configure == null) continue;
            Class<?> type = field.getType();
            String path = configure.value().equals("") ? field.getName() : configure.value();
            field.setAccessible(true);
            Object value;
            var extractor = AnnotationConfig.getSpecialExtractors().get(type);
            if (extractor != null) value = extractor.apply(field, section, path);
            else value = AnnotationConfig.getConverters().get(type).apply(section.getString(path));
            field.set(null, value);
        }
    }

}
