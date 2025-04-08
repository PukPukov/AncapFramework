package ru.ancap.framework.database.nosql.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString @EqualsAndHashCode(callSuper = true)
public class IllegalEntryReadException extends RuntimeException {
}