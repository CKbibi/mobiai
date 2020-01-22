package com.cex0.mobiai.core.view;

import com.cex0.mobiai.util.ThreadParameterUtil;

import java.io.Serializable;

public class ResultModel implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 初始给返回状态赋值
     */
    static {
        SUCCESS_CODE = ResponseCodeEnum.SUCCESS.getCode();
        ERROR_CODE = ResponseCodeEnum.BIZ_ERROR.getCode();
        WARN_CODE = ResponseCodeEnum.WARN.getCode();
        WARN_OPERATOR_CODE = ResponseCodeEnum.WARN_OPERATOR.getCode();
    }


    /**
     * 返回成功
     */
    public static final String SUCCESS_CODE;

    /**
     * 返回失败
     */
    public static final String ERROR_CODE;

    /**
     * 返回失败
     */
    public static final String WARN_CODE;

    /**
     * 返回失败
     */
    public static final String WARN_OPERATOR_CODE;

    /**
     * 返回代码
     */
    private String code;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 错误代码
     */
    private String error;

    /**
     * 请求id
     */
    private String requestId;

    public ResultModel(String code, Object data, String message, String error) {
        this.code = SUCCESS_CODE;
        this.code = code;
        this.data = data;
        this.message = message;
        this.requestId = ThreadParameterUtil.get("requestId");
        this.error = error;
    }

    public static ResultModel success(String message, Object data) {
        return new ResultModel(SUCCESS_CODE, data, message, "");
    }

    public static ResultModel success(Object data) {
        return new ResultModel(SUCCESS_CODE, data, "", "");
    }

    public static ResultModel success() {
        return new ResultModel(SUCCESS_CODE, (Object)null, "", "");
    }

    public static ResultModel fail(String message, String error) {
        return new ResultModel(ERROR_CODE, (Object)null, message, error);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
