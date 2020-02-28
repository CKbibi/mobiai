package com.cex0.mobiai.exception;

import io.undertow.util.BadRequestException;

public class MissingPropertyException extends BadRequestException {

    public MissingPropertyException(String message) {
        super(message);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
