package ru.ancap.framework.command.api.commands.object.dispatched;

import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;

import java.util.List;
import java.util.function.Supplier;

/**
 * <h1>Контракт</h1>
 * <h3>Части и нумерация частей</h3>
 * <code>parts()</code> представляет из себя упорядоченные части включая первичную часть. Таким образом в <code>/foo bar baz</code> 3 части, [foo, bar, baz].
 * <h3>Обход</h3>
 * LeveledCommand содержит в себе "счётчик обхода" — произвольно созданные данные для упрощения парсинга команды. Созданная внешним образом LeveledCommand всегда
 * начинает с нулевым счётчиком указывающим на первую часть.<br>
 * Можно шагнуть вперёд, увеличив счётчик на один с помощью метода <code>step()</code>.<br>
 * Возвращаемый <code>ParseState</code> содержит часть, на которую будет указывать счётчик <i>после</i> шага, а также соответствующий этой части LeveledCommand
 * с соответствующим номером счётчика.
 */
public interface LeveledCommand {
    
    String original();
    List<Part> parts();
    int currentPartIndex();
    
    LCParseState step(Supplier<? extends Throwable> ifNo);
    
    // UTIL
    default boolean isRaw() {
        return this.currentPartIndex()+1 == this.parts().size();
    }
    
    default Part currentPart() {
        return this.parts().get(this.currentPartIndex());
    }
    
    /**
     * Obtains next argument directly and unsafe.
     */
    default Part nextPart() {
        return this.parts().get(currentPartIndex()+1);
    }
    
    default List<Part> allNextParts() {
        return this.isRaw() ? List.of() : this.parts().subList(this.currentPartIndex()+1, this.parts().size());
    }
    
    default LCParseState asParseState() {
        return new LCParseState(this.currentPart(), this);
    }
    
    // CONVENIENCE
    default LCParseState step() {
        return step(NoNextArgumentException::new);
    }
}