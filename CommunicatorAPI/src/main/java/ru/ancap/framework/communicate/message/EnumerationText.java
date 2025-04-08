package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class EnumerationText<T> extends CacheText {
    
    public EnumerationText(Map<String, T> data, BiFunction<String, T, CallableText> mapper) {
        super(name -> {
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, T> entry : data.entrySet()) {
                result.add(mapper.apply(entry.getKey(), entry.getValue()).call(name));
            }
            return String.join("\n", result);
        });
    }
    
}