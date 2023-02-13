package ru.ancap.framework.command.api.commands.object.dispatched;

import java.util.List;

public interface LeveledCommand {

    boolean isRaw();
    
    List<String> arguments();
    
    String nextArgument();
    List<String> nextArguments(int arguments);
    
    LeveledCommand withoutArgument();
    LeveledCommand withoutArguments(int arguments);

}
