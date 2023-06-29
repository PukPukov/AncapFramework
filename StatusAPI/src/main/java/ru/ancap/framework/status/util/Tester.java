package ru.ancap.framework.status.util;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.speak.dialogue.Question;

@AllArgsConstructor
public class Tester {
    
    private final Communicator communicator;
    
    public void askQuestion(CallableMessage message) {

        Assertions.assertTrue(new Question(message).ask(this.communicator));
        
    }
    
}
