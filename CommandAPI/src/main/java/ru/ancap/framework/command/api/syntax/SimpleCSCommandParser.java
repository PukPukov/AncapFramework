package ru.ancap.framework.command.api.syntax;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.commons.TodoException;

@ToString @EqualsAndHashCode
public class SimpleCSCommandParser implements CSCommandParser {
    
    @Override
    public CSCommand parse(String command) {
        throw new TodoException();
    }
    
}