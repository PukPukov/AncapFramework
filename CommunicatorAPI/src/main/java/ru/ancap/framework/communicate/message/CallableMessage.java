package ru.ancap.framework.communicate.message;

public interface CallableMessage {

    /**
     * @return String in minimessage format.
     */
    String call(String identifier);
    
}
