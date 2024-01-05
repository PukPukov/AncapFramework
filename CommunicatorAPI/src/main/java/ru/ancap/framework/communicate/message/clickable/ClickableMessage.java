package ru.ancap.framework.communicate.message.clickable;

import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.WrapperMessage;

import java.util.function.Consumer;

public class ClickableMessage extends WrapperMessage implements CallableMessage {
    
    public static ActionMessageProvider provider;

    public ClickableMessage(CallableMessage base, Consumer<Click> clickConsumer) {
        super(__(base, clickConsumer));
    }

    private static CallableMessage __(CallableMessage base, Consumer<Click> clickConsumer) {
        return ClickableMessage.provider.to(base, clickConsumer);
    }

}
