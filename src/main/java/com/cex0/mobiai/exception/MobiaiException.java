package com.cex0.mobiai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 *  基本异常类
 */
public abstract class MobiaiException extends RuntimeException {

    /**
     * 错误信息
     */
    private Object errorData;

    public MobiaiException(String message) {
        super(message);
    }

    public MobiaiException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    /**
     * 设置错误信息
     *
     * @param errorData 错误信息
     * @return          异常错误
     */
    @NonNull
    public MobiaiException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
