package ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.ArgumentExtractor;

import java.util.List;

@AllArgsConstructor
@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class TransformationException extends Exception {

    private final ArgumentExtractor<?> argumentExtractor;
    private final List<String> base;

    public ArgumentExtractor<?> extractor() {
        return this.argumentExtractor;
    }
    
    public List<String> base() {
        return this.base;
    }

}