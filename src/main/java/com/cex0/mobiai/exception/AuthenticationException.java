package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;

/**
 * 身份验证异常类
 *
 * @author Cex0
 */
public class AuthenticationException extends MobiaiException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
