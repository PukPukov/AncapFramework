package ru.ancap.framework.communicate.message.clickable;


import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.function.Consumer;

public interface ActionMessageProvider {
    
    CallableMessage to(CallableMessage base, Consumer<Click> with);
    
}
