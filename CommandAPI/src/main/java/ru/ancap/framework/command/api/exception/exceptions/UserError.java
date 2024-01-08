package ru.ancap.framework.command.api.exception.exceptions;

/**
 * Error, appeared because of user failure. Similar to 400's http codes.
 */
public abstract class UserError extends RuntimeException {

    public static volatile boolean DEBUG = false;

    @Override
    public Throwable fillInStackTrace() {
        if (DEBUG) return super.fillInStackTrace();
        else return this; // as far as I know this should significantly optimize exceptions, and this is
                          // appropriate since all UserErrors should be handled without stacktrace usage by
                          // exception reactor
    }

}
