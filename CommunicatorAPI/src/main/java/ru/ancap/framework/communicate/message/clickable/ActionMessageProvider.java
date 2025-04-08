package ru.ancap.framework.communicate.message.clickable;


import ru.ancap.framework.communicate.message.CallableText;

import java.util.function.Consumer;

public interface ActionMessageProvider {
    
    CallableText to(CallableText base, Consumer<Click> with);
    
}