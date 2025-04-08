package ru.ancap.framework.plugin.api.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.StandardException;

@StandardException
@ToString @EqualsAndHashCode(callSuper = true)
public class CommandNotRegisteredException extends RuntimeException {}