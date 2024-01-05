package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@ToString(callSuper = true) @EqualsAndHashCode
@RequiredArgsConstructor
public class EnumerationMessage<T> implements CallableMessage {
    
    private final Map<String, T> data;
    private final BiFunction<String, T, CallableMessage> mapper;
    
    @Override
    public String call(String receiverId) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, T> entry : this.data.entrySet()) {
            result.add(this.mapper.apply(entry.getKey(), entry.getValue()).call(receiverId));
        }
        return String.join("\n", result);
    }
    
}
