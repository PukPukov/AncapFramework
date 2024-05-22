package ru.ancap.framework.status.util.exception;

import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.CAPIDescribedException;

public class HandTestFailureException extends CAPIDescribedException {
    
    public HandTestFailureException(CallableMessage failedCheckMessage) {
        super(new LAPIMessage(
            CommonMessageDomains.Test.handTestFailure,
            new Placeholder("failed check message", failedCheckMessage)
        ));
    }
    
}