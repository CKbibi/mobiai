package com.cex0.mobiai.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * 运行模式
 * @author Cex0
 * @date   2020年1月22日
 */
public enum Mode {

    PRODUCTION,
    DEVELOPMENT,
    TEST;

    @JsonValue
    String getValue() {
        return this.name().toLowerCase();
    }


    /**
     * 从值中获取模式
     * @param value
     * @return
     */
    @Nullable
    @JsonCreator
    public static Mode valueFrom(@Nullable String value) {
        if (StringUtils.isBlank(value) || "prod".equalsIgnoreCase(value)) {
            return Mode.PRODUCTION;
        }

        if ("dev".equalsIgnoreCase(value)) {
            return Mode.DEVELOPMENT;
        }

        if ("test".equalsIgnoreCase(value)) {
            return Mode.TEST;
        }

        return null;
    }
}
