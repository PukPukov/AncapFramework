package ru.ancap.framework.communicate.communicator;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.List;

@RequiredArgsConstructor
public class MultiCommunicator implements Communicator {
    
    private final List<Communicator> communicators;

    @Override
    public void message(CallableMessage message) {
        for (Communicator communicator : this.communicators) communicator.message(message);
    }

}
