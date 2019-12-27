package com.cex0.mobiai.exception;

public enum ResponseCodeEnum {
    SUCCESS("00000", "SUCCESS"),
    API_KEY_NOT_AUTH("101", "API_KEY_NOT_AUTH"),
    CALLTIME_EXPIRED("102", "CALLTIME_EXPIRED"),
    KEY_NOT_NULL("103", "KEY_NOT_NULL"),
    VALUE_NOT_NULL("104", "VALUE_NOT_NULL"),
    WARN("10000", "WARN"),
    WARN_OPERATOR("10001", "WARN"),
    BIZ_ERROR("300", "BIZ_ERROR"),
    BIZ_DONT_USE("3000", "BIZ_ERROR"),
    IS_EXIST("301", "IS_EXIST"),
    NAME_IS_EXIST("302", "NAME_IS_EXIST"),
    DB_TIMEOUT("303", "DB_TIMEOUT"),
    DB_BUSY("304", "DB_BUSY"),
    API_ERROR("00001", "API_ERROR"),
    SQL_ERROR("00002", "SQL_ERROR"),
    HTTP_ERROR("00003", "HTTP_ERROR"),
    HTTP_BIZ_ERROR("00014", "HTTP_BIZ_ERROR"),
    HTTPS_ERROR("00013", "HTTPS_ERROR"),
    WS_ENTER_ERROR("00004", "WS_ENTER_ERROR"),
    WS_OUTER_ERROR("00005", "WS_OUTER_ERROR"),
    PARAMETER_EXCEPTION("00006", "PARAMETER_EXCEPTION"),
    I18N_ERROR("00007", "I18N_ERROR"),
    ERROR("00009", "ERROR"),
    REDIS_ERROR("00010", "REDIS_ERROR"),
    SYS_PARAM_NULL("00011", "SYS_PARAM_NULL"),
    BIZ_PARAM_NULL("00012", "BIZ_PARAM_NULL");

    private String code;
    private String messageKey;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    private ResponseCodeEnum(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
