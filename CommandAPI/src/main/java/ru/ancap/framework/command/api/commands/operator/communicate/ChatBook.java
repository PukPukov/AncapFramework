package ru.ancap.framework.command.api.commands.operator.communicate;

import lombok.AllArgsConstructor;
import ru.ancap.framework.communicate.message.CallableText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class ChatBook<LISTED> implements CallableText {
    
    private final Iterable<LISTED> content;
    private final Function<LISTED, CallableText> provider;

    @Override
    public String call(String identifier) {
        List<String> result = new ArrayList<>();
        for (LISTED listed : this.content) result.add(this.provider.apply(listed).call(identifier));
        return String.join("\n", result);
    }
    
}