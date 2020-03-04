package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 15:29
 * @Description:
 */
public class ForbiddenException extends MobiaiException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
