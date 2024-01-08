package ru.ancap.framework.command.api.syntax;

import ru.ancap.commons.TodoException;

public class SimpleCSCommandParser implements CSCommandParser {
    
    @Override
    public CSCommand parse(String command) {
        throw new TodoException();
    }
    
}
