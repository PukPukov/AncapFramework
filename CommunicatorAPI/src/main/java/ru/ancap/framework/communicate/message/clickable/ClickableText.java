package ru.ancap.framework.communicate.message.clickable;

import ru.ancap.framework.communicate.message.CacheText;
import ru.ancap.framework.communicate.message.CallableText;

import java.util.function.Consumer;

public class ClickableText extends CacheText implements CallableText {
    
    public static ActionMessageProvider provider;
    
    public ClickableText(CallableText base, Consumer<Click> clickConsumer) {
        super(ClickableText.constructorHelper(base, clickConsumer));
    }

    private static CallableText constructorHelper(CallableText base, Consumer<Click> clickConsumer) {
        return ClickableText.provider.to(base, clickConsumer);
    }

}