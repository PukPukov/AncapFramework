package ru.ancap.framework.plugin.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@Accessors(fluent = true)
public class InDevMessage {
    
    @Getter
    private static final CallableMessage instance = new LAPIMessage(CommonMessageDomains.inDev);
    
}