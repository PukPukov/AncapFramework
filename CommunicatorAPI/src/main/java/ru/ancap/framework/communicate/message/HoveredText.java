package ru.ancap.framework.communicate.message;

import ru.ancap.framework.communicate.modifier.Placeholder;

public class HoveredText extends CacheText implements CallableText {
    
    public HoveredText(CallableText base, CallableText hover) {
        super(new Text(
            "<hover:show_text:'%H%'>%T%</hover>",
            new Placeholder("t", base), // text
            new Placeholder("h", hover) // hover
        ));
    }
    
}