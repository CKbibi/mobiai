package com.cex0.mobiai.model.properties;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 16:52
 * @Description: Aip properties
 */
public enum ApiProperties implements PropertyEnum{

    /**
     * api_enabled
     */
    API_ENABLED("api_enabled", Boolean.class, "false"),

    /**
     * api_access_key
     */
    API_ACCESS_KEY("api_access_key", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    ApiProperties(String value, Class<?> type, String defaultValue) {
        this.defaultValue = defaultValue;
        this.type = type;
        this.value = value;
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
