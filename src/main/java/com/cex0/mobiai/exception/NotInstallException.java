package com.cex0.mobiai.exception;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 22:55
 * @Description:
 */
public class NotInstallException extends BadRequestException {

    public NotInstallException(String message) {
        super(message);
    }

    public NotInstallException(String message, Throwable cause) {
        super(message, cause);
    }
}
