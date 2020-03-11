package com.cex0.mobiai.model.enums;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:53
 * @Description:
 */
public enum PostPermalinkType implements ValueEnum<Integer> {

    /**
     * /archives/${slug}
     */
    DEFAULT(0),

    /**
     * /1970/01/01/${slug}
     */
    DATE(1),

    /**
     * /1970/01/${slug}
     */
    DAY(2),

    /**
     * /?p=${id}
     */
    ID(3);


    private Integer value;

    PostPermalinkType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
