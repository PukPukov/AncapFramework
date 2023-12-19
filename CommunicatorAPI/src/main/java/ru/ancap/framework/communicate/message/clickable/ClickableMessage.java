package ru.ancap.framework.communicate.message.clickable;

import ru.ancap.framework.communicate.message.CacheMessage;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.function.Consumer;

public class ClickableMessage extends CacheMessage implements CallableMessage {
    
    public static ActionMessageProvider provider;

    public ClickableMessage(CallableMessage base, Consumer<Click> clickConsumer) {
        super(__(base, clickConsumer));
    }

    private static CallableMessage __(CallableMessage base, Consumer<Click> clickConsumer) {
        return ClickableMessage.provider.to(base, clickConsumer);
    }

}
