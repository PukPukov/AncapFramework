package ru.ancap.framework.communicate.message;

import ru.ancap.framework.communicate.modifier.Placeholder;

public class HoveredMessage extends CacheMessage implements CallableMessage {
    
    public HoveredMessage(CallableMessage base, CallableMessage hover) {
        super(new Message(
            "<hover:show_text:'%H%'>%T%</hover>",
            new Placeholder("t", base), // text
            new Placeholder("h", hover) // hover
        ));
    }
    
}
