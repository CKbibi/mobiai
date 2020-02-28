package com.cex0.mobiai.model.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * 枚举值接口
 *
 * @author Cex0
 */
public interface ValueEnum<T> {

    /**
     * 将值转换成相应类型的enum
     * @param enumType  枚举类型
     * @param value     值
     * @param <V>       通用类型值
     * @param <E>       通用类型枚举
     * @return
     */
    static <V, E extends ValueEnum<V>> E valueToEnum(Class<E> enumType, V value) {
        Assert.notNull(enumType, "enum type must not be null");
        Assert.notNull(value, "value type must not be null");
        Assert.isTrue(enumType.isEnum(), "type must be an enum type");

        return Stream.of(enumType.getEnumConstants())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown database value: " + value));
    }


    /**
     * 获取enum值
     *
     * @return 枚举值
     */
    T getValue();
}
