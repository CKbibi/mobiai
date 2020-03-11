package com.cex0.mobiai.model.enums;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:17
 * @Description:
 */
public enum OptionType implements ValueEnum<Integer> {

    /**
     * 内部option
     */
    INTERNAL(0),

    /**
     * 用户自定义option
     */
    CUSTOM(1);


    private Integer value;

    OptionType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
