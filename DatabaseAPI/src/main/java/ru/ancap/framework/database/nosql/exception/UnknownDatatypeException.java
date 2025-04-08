package ru.ancap.framework.database.nosql.exception;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class UnknownDatatypeException extends RuntimeException {
    
    private final String path;
    private final String unknownType;
    
    @Override public String getMessage() {
        return "\n"+
            "At path \""+this.path+"\"\n"+
            "there is entry with unknown type \""+this.unknownType+"\"\n";
    }
    
}