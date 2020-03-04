package com.cex0.mobiai.cache;

import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @param <K> 缓存key类型
 * @param <V> 缓存value类型
 * @Date: 2020/2/28 23:06
 * @Description: 缓存池接口
 */
public interface CacheStore<K, V> {

    /**
     * 通过key获取value
     *
     * @param key key不为空
     * @return
     */
    Optional<V> get(@NonNull K key);


    /**
     * 放置将过期的缓存。
     *
     * @param key           缓存key不能为空
     * @param value         缓存value不能为空
     * @param timeout       过期时间不能小于1
     * @param timeUnit      超时单位
     */
    void put(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);


    /**
     * 放置一个缓存，如果密钥不存在，该缓存将过期。
     *
     * @param key           缓存key不能为空
     * @param value         缓存value不能为空
     * @param timeout       过期时间不能小于1
     * @param timeUnit      超时单位
     * @return              如果密钥不存在并且设置了值，则为true；如果之前存在密钥，则为false；如果有任何其他原因，则为null
     */
    Boolean putIfAbsent(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);


    /**
     * 放置未过期的缓存。
     *
     * @param key           缓存key不能为空
     * @param value         缓存value不能为空
     */
    void put(@NonNull K key, @NonNull V value);


    /**
     * 删除
     *
     * @param key           缓存key不能为空
     */
    void delete(@NonNull K key);
}
