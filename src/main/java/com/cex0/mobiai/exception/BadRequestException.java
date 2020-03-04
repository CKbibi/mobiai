package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 22:57
 * @Description:
 */
public class BadRequestException extends MobiaiException {


    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
