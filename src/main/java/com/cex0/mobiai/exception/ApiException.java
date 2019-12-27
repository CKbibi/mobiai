package com.cex0.mobiai.exception;

import com.cex0.mobiai.core.view.ResultModel;
import com.cex0.mobiai.util.ThreadParameterUtil;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ApiException extends RuntimeException {
    private ResponseCodeEnum responseCodeEnum;

    private Object[] params;

    private String error;


    public ApiException() {
    }


    private ApiException(ResponseCodeEnum responseCodeEnum, String error, String... params) {
        super(responseCodeEnum.getCode());
        this.responseCodeEnum = responseCodeEnum;
        this.error = error;
        this.params = params;
    }


    private ApiException(String messageKey, String error, Object... params) {
        super(messageKey);
        this.params = params;
        this.error = error;
        this.responseCodeEnum = ResponseCodeEnum.BIZ_ERROR;
        this.responseCodeEnum.setMessageKey(messageKey);
    }


    private ApiException(Integer code, String messageKey, String error, Object... params) {
        super(messageKey);
        if (code >= 100) {
            code = 0;
        }

        code = Integer.parseInt(ResponseCodeEnum.BIZ_ERROR.getCode()) * 100 + code;
        this.params = params;
        this.error = error;
        this.responseCodeEnum = ResponseCodeEnum.BIZ_DONT_USE;
        this.responseCodeEnum.setMessageKey(messageKey);
        this.responseCodeEnum.setCode(code.toString());
    }

    private ApiException(ResponseCodeEnum responseCodeEnum, String error, Object... params) {
        super(responseCodeEnum.getCode());
        this.responseCodeEnum = responseCodeEnum;
        this.error = error;
        this.params = params;
    }


    public static ApiException createCommonEx(ResponseCodeEnum responseCodeEnum, String error, Object... params) {
        ThreadParameterUtil.refreshParameter("baseName", "i18n.message");
        return new ApiException(responseCodeEnum, error, params);
    }

    public static ApiException createCommonBizEx(String messageKey, String error, Object... params) {
        ThreadParameterUtil.refreshParameter("baseName", "i18n.service");
        return new ApiException(messageKey, error, params);
    }

    public static ApiException createBizEx(String messageKey, Object... params) {
        return createCommonBizEx(messageKey, "", params);
    }

    public static ApiException createBizEx(Integer code, String messageKey, Object... params) {
        ThreadParameterUtil.refreshParameter("baseName", "i18n.service");
        return new ApiException(code, messageKey, "", params);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public String getCode() {
        return this.responseCodeEnum.getCode();
    }

    public ResponseCodeEnum getResponseCodeEnum() {
        return this.responseCodeEnum;
    }

    public String getError() {
        return this.error;
    }

    public Object[] getParams() {
        return this.params;
    }

}
