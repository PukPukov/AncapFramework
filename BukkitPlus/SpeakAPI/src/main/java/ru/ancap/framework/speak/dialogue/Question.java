package ru.ancap.framework.speak.dialogue;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Blocking;
import ru.ancap.commons.multithreading.WaitFor;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.HoveredText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.message.MultilineText;
import ru.ancap.framework.communicate.message.clickable.ClickableText;
import ru.ancap.framework.communicate.modifier.ArgumentPlaceholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.function.Function;

@AllArgsConstructor
public class Question {
    
    private final CallableText questionMessage;
    
    @Blocking
    public boolean ask(Communicator communicator) {
        WaitFor<Boolean> wait = new WaitFor<>();
        communicator.message(new MultilineText(
            this.questionMessage,
            new LAPIText(
                CommonMessageDomains.yesNo,
                new ArgumentPlaceholder("yes", this.answerFunctionFor(wait, true)),
                new ArgumentPlaceholder("no", this.answerFunctionFor(wait, false))
            )
        ));
        return wait.get();
    }

    private Function<String, CallableText> answerFunctionFor(WaitFor<Boolean> wait, boolean boolean_) {
        return (argument) -> new ClickableText(
            new HoveredText(
                new Text(argument),
                new LAPIText(CommonMessageDomains.clickToSelect)
            ),
            click -> wait.put(boolean_)
        );
    }

}