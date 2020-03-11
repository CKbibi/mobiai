package com.cex0.mobiai.model.properties;

/**
 * 邮箱参数
 *
 * @author Cex0
 * @date 2020/03/11
 */
public enum EmailProperties implements PropertyEnum {

    HOST("email_host", String.class, ""),

    PROTOCOL("email_protocol", String.class, "smtp"),

    SSL_PORT("email_ssl_port", Integer.class, "465"),

    USERNAME("email_username", String.class, ""),

    PASSWORD("email_password", String.class, ""),

    FROM_NAME("email_from_name", String.class, ""),

    ENABLED("email_enabled", Boolean.class, "false");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;


    EmailProperties(String value, Class<?> type, String defaultValue) {
        if (!PropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported blog property type: " + type);
        }

        this.defaultValue = defaultValue;
        this.value = value;
        this.type = type;
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
