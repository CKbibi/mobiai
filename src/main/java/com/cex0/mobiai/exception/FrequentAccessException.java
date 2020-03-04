package com.cex0.mobiai.exception;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 23:39
 * @Description:
 */
public class FrequentAccessException extends BadRequestException{

    public FrequentAccessException(String message) {
        super(message);
    }

    public FrequentAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
