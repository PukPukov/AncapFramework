package ru.ancap.framework.command.api.commands.exception.exception;


import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class UnawaitedRawCommandException extends RuntimeException {
}