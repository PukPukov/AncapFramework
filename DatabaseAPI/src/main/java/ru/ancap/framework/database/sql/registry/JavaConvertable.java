package ru.ancap.framework.database.sql.registry;

public interface JavaConvertable<ID, JAVA> {
    
    ID id();
    JAVA toJava();
    
}