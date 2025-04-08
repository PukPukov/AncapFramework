package ru.ancap.framework.status.util;

import lombok.AllArgsConstructor;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.speak.dialogue.Question;
import ru.ancap.framework.status.util.exception.HandTestFailureException;

@AllArgsConstructor
public class Tester {
    
    private final Communicator communicator;
    
    public void askQuestion(CallableText message) {
        var question = new Question(message);
        boolean success = question.ask(this.communicator);
        if (!success) throw new HandTestFailureException(message);
    }
    
}