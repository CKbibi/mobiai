package com.cex0.mobiai.exception;

/**
 * @Auther: Cex0
 * @Date: 2019/12/24 22:47
 * @Description:  异常定义的枚举类
 */
public enum ExceptionEnum implements CommonError {
    /**
     * 参数不合法异常
     */
    PARAMETER_VALIDTION_ERROR(10001, "参数不合法"),
    UNKNOW_ERROR(10002, "未知错误");
    ;

    private int errCode;
    private String errMsg;

    private ExceptionEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
