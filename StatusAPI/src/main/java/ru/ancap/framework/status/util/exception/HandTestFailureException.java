package ru.ancap.framework.status.util.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.CAPIDescribedException;

@ToString @EqualsAndHashCode(callSuper = true)
public class HandTestFailureException extends CAPIDescribedException {
    
    public HandTestFailureException(CallableText failedCheckMessage) {
        super(new LAPIText(
            CommonMessageDomains.Test.handTestFailure,
            new Placeholder("failed check message", failedCheckMessage)
        ));
    }
    
}