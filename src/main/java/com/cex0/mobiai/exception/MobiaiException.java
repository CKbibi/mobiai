package com.cex0.mobiai.exception;

import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基本的异常类
 *
 * @author Cex0
 */
public class MobiaiException extends Exception implements CommonError {

    private CommonError commonError;


    /**
     * 构造函数传入一个commonError接口的实现
     * @param commonError
     */
    @NonNull
    public MobiaiException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }


    /**
     * 构造函数传入commonError接口实现和错误信息
     * @param commonError   传入异常
     * @param errMsg        异常信息
     */
    public MobiaiException(@NonNull CommonError commonError, @Nullable String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
