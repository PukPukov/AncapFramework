package ru.ancap.framework.communicate.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class EnumerationMessage<T> extends WrapperMessage {
    
    public EnumerationMessage(Map<String, T> data, BiFunction<String, T, CallableMessage> mapper) {
        super(name -> {
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, T> entry : data.entrySet()) {
                result.add(mapper.apply(entry.getKey(), entry.getValue()).call(name));
            }
            return String.join("\n", result);
        });
    }
    
}
