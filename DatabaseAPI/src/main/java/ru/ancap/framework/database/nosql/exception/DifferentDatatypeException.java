package ru.ancap.framework.database.nosql.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@ToString @EqualsAndHashCode(callSuper = true)
public class DifferentDatatypeException extends RuntimeException {
    
    private final String path;
    private final Class<?> expected;
    private final Class<?> actual;
    
    @Override public String getMessage() {
        return "\n"+
            "At path \""+this.path+"\"\n"+
            "we expected type \""+this.expected.getName()+"\"\n"+
            "but there was \""+this.actual.getName()+"\"";
    }
    
}