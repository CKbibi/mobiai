package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/29 00:17
 * @Description:
 */
public class ServiceException extends MobiaiException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
