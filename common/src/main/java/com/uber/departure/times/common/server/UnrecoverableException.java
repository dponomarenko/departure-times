package com.uber.departure.times.common.server;

/**
 * @author Danila Ponomarenko
 */
public final class UnrecoverableException extends RuntimeException {
    public UnrecoverableException(String message) {
        super(message);
    }

    public UnrecoverableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnrecoverableException(Throwable cause) {
        super(cause);
    }
}
