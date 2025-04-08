package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode @ToString
@RequiredArgsConstructor
public class MultilineText implements CallableText {
    
    private final List<CallableText> toMerge;
    
    public MultilineText(CallableText... messages) {
        this(List.of(messages));
    }

    @Override
    public String call(String identifier) {
        return this.toMerge.stream()
            .map(message -> message.call(identifier))
            .collect(Collectors.joining("\n"));
    }
    
}