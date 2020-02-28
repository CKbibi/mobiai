package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;

/**
 * 没有找到实体异常
 *
 * @author Cex0
 */
public class NotFoundException extends MobiaiException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
