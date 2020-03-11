package com.cex0.mobiai.exception;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:03
 * @Description:
 */
public class EmailException extends ServiceException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
