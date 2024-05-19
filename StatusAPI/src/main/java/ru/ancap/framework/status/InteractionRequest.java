package ru.ancap.framework.status;

import ru.ancap.framework.communicate.message.WrapperMessage;
import ru.ancap.framework.communicate.modifier.Modifier;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.List;

public class InteractionRequest extends WrapperMessage {
    
    public InteractionRequest(
        String requiredActionId, List<Modifier> requestedActionMessageModifiers,
        String expectedResultId, List<Modifier> expectedResultMessageModifiers
    ) {
        super(new LAPIMessage(
            CommonMessageDomains.Test.root+".base.interact",
            new Placeholder("action", new LAPIMessage(
                CommonMessageDomains.Test.root+".checks.action."+requiredActionId,
                requestedActionMessageModifiers.toArray(new Modifier[0])
            )),
            new Placeholder("expected", new LAPIMessage(
                CommonMessageDomains.Test.root+".checks.expected.interact."+expectedResultId,
                expectedResultMessageModifiers.toArray(new Modifier[0])
            ))
        ));
    }
}