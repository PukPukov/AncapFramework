package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode @ToString
public class MultilineMessage implements CallableMessage {
    
    private final List<CallableMessage> toMerge;
    
    public MultilineMessage(CallableMessage... messages) {
        this.toMerge = List.of(messages);
    }

    @Override
    public String call(String receiverId) {
        return this.toMerge.stream()
            .map(message -> message.call(receiverId))
            .collect(Collectors.joining("\n"));
    }
    
}
