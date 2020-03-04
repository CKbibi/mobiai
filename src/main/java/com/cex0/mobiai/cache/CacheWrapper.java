package com.cex0.mobiai.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 23:27
 * @Description: Cache包装器.
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CacheWrapper<V> implements Serializable {

    /**
     * Cache data
     */
    private V date;

    /**
     * 过期时间
     */
    private Date expireAt;

    /**
     * 创建时间
     */
    private Date createAt;
}
