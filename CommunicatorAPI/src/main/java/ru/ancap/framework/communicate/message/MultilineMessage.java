package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode @ToString
@RequiredArgsConstructor
public class MultilineMessage implements CallableMessage {
    
    private final List<CallableMessage> toMerge;
    
    public MultilineMessage(CallableMessage... messages) {
        this(List.of(messages));
    }

    @Override
    public String call(String identifier) {
        return this.toMerge.stream()
            .map(message -> message.call(identifier))
            .collect(Collectors.joining("\n"));
    }
    
}