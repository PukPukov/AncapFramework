package ru.ancap.framework.api.command.commands.transformer;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransformationException extends Exception {

    private final String argument;
    private final Class<?> type;

}