package ru.ancap.framework.communicate.message;

public interface CallableMessage {

    /**
     * By contract implementation methods should do nothing more that message creation. If possible, method should be pure.
     * @return String in minimessage format.
     */
    String call(String receiverId);

    // Implementations should support proper equals(), hashCode() and toString().
    boolean equals(Object object);
    int hashCode();
    String toString();
    
}
