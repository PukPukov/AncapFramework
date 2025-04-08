package ru.ancap.framework.plugin.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@Accessors(fluent = true)
public class InDevMessage {
    
    @Getter
    private static final CallableText instance = new LAPIText(CommonMessageDomains.inDev);
    
}