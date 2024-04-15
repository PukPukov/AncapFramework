package ru.ancap.framework.database.sql.registry;

public interface SQLConvertable<ID, SQL> {
    
    SQL toSQL(ID id);
    
}