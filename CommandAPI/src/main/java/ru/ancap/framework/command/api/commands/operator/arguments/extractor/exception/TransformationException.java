package ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception;

import lombok.AllArgsConstructor;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.ArgumentExtractor;

import java.util.List;

@AllArgsConstructor
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