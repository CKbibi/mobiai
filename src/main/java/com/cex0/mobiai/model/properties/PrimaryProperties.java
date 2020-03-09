package com.cex0.mobiai.model.properties;

import static com.cex0.mobiai.model.support.MobiaiConst.DEFAULT_THEME_ID;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 22:41
 * @Description: 初始化参数
 */
public enum  PrimaryProperties implements PropertyEnum {

    IS_INSTALLED("is_installed", Boolean.class, "false"),

    THEME("theme", String.class, DEFAULT_THEME_ID),

    BIRTHDAY("birthday", Long.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    PrimaryProperties(String value, Class<?> type, String defaultValue) {
        this.value = value;
        this.type = type;
        this.defaultValue = defaultValue;
    }


    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }
}
