package ru.ancap.framework.command.api.exception.exceptions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnhandledException extends RuntimeException {

    private final Throwable unhandledException;

}
