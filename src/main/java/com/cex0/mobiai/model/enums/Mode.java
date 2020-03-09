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
    DEMO,
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
    @JsonCreator
    public static Mode valueFrom(@Nullable String value) {
        Mode modeResult = null;
        for (Mode mode : values()) {
            if (mode.name().equalsIgnoreCase(value)) {
                modeResult = mode;
                break;
            }
        }

        if (modeResult == null) {
            modeResult = PRODUCTION;
        }

        return modeResult;
    }
}
