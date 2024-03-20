package ru.ancap.framework.speak.dialogue;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Blocking;
import ru.ancap.commons.multithreading.WaitFor;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.HoveredMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.message.MultilineMessage;
import ru.ancap.framework.communicate.message.clickable.ClickableMessage;
import ru.ancap.framework.communicate.modifier.ArgumentPlaceholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.function.Function;

@AllArgsConstructor
public class Question {
    
    private final CallableMessage questionMessage;
    
    @Blocking
    public boolean ask(Communicator communicator) {
        WaitFor<Boolean> wait = new WaitFor<>();
        communicator.message(new MultilineMessage(
            this.questionMessage,
            new LAPIMessage(
                CommonMessageDomains.yesNo,
                new ArgumentPlaceholder("yes", this.answerFunctionFor(wait, true)),
                new ArgumentPlaceholder("no", this.answerFunctionFor(wait, false))
            )
        ));
        return wait.get();
    }

    private Function<String, CallableMessage> answerFunctionFor(WaitFor<Boolean> wait, boolean boolean_) {
        return (argument) -> new ClickableMessage(
            new HoveredMessage(
                new Message(argument),
                new LAPIMessage(CommonMessageDomains.clickToSelect)
            ),
            click -> wait.put(boolean_)
        );
    }

}
