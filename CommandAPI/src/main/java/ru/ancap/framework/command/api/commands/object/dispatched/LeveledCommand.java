package ru.ancap.framework.command.api.commands.object.dispatched;

import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;

import java.util.List;
import java.util.function.Supplier;

public interface LeveledCommand {

    boolean isRaw();
    
    List<String> arguments();
    
    default String nextArgument() {
        return nextArgument(NoNextArgumentException::new);
    }
    
    default String nextArgument(Supplier<? extends Throwable> ifNo) {
        return nextArguments(1, ifNo).getFirst();
    }
    
    default List<String> nextArguments(int arguments) {
        return nextArguments(arguments, NoNextArgumentException::new);
    }
    
    List<String> nextArguments(int arguments, Supplier<? extends Throwable> ifNo);
    
    LeveledCommand withoutArgument();
    LeveledCommand withoutArguments(int arguments);

}