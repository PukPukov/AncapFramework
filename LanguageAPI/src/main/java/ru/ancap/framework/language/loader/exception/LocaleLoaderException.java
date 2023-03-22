package ru.ancap.framework.language.loader.exception;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class LocaleLoaderException extends RuntimeException {

    public LocaleLoaderException(String s) {
        super(s);
    }
}
