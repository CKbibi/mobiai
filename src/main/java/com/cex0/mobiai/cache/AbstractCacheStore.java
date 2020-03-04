package com.cex0.mobiai.cache;

import com.cex0.mobiai.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 23:05
 * @Description:
 */
@Slf4j
public abstract class AbstractCacheStore<K, V> implements CacheStore<K, V> {

    /**
     * 通过key获取缓存包装器。
     *
     * @param key       key不能为空
     * @return          缓存包装器
     */
    @NonNull
    abstract Optional<CacheWrapper<V>> getInternal(@NonNull K key);


    /**
     * 插入wrapper
     *
     * @param key           key不能为空
     * @param cacheWrapper  缓存包装器不能为空
     */
    abstract void putInternal(@NonNull K key, @NonNull CacheWrapper<V> cacheWrapper);


    /**
     * 如果不存在就存入
     *
     * @param key           key不能为空
     * @param cacheWrapper  缓存包装器不能为空
     * @return 如果密钥不存在并且设置了值，则为true；如果之前存在密钥，则为false；如果有任何其他原因，则为null
     */
    abstract Boolean putInternalIfAbsent(@NonNull K key, @NonNull CacheWrapper<V> cacheWrapper);


    @Override
    public Optional<V> get(K key) {
        Assert.notNull(key, "Cache key must not be null!");

        return getInternal(key).map(cacheWrapper -> {
            //查看是否过期
           if (cacheWrapper.getExpireAt() != null && cacheWrapper.getExpireAt().before(DateUtils.now())) {
               log.warn("Cache key: [{}] has been expired!", key);

               // 删除key
               delete(key);

               //返回
               return null;
           }

           return cacheWrapper.getDate();
        });
    }


    @Override
    public void put(K key, V value, long timeout, TimeUnit timeUnit) {
        putInternal(key, buildCacheWrapper(value, timeout, timeUnit));
    }


    @Override
    public Boolean putIfAbsent(K key, V value, long timeout, TimeUnit timeUnit) {
        return putInternalIfAbsent(key, buildCacheWrapper(value, timeout, timeUnit));
    }


    @Override
    public void put(K key, V value) {
        putInternal(key, buildCacheWrapper(value, 0, null));
    }


    /**
     * 生成缓存包装器
     *
     * @param value         value不能为空
     * @param timeout       密钥过期时间，如果过期时间小于1，则缓存不会过期
     * @param timeUnit      超时单位必须
     * @return
     */
    private CacheWrapper<V> buildCacheWrapper(@NonNull V value, long timeout, @Nullable TimeUnit timeUnit) {
        Assert.notNull(value, "CacheValue must not be null!");
        Assert.isTrue(timeout >= 0, "Cache expiration timeout must not be than 1");

        Date now = DateUtils.now();

        Date expireAt = null;

        if (timeout > 0 && timeUnit != null) {
            expireAt = DateUtils.add(now, timeout, timeUnit);
        }

        // 生成缓存包装器
        CacheWrapper<V> cacheWrapper = new CacheWrapper<>();
        cacheWrapper.setCreateAt(now);
        cacheWrapper.setExpireAt(expireAt);
        cacheWrapper.setDate(value);

        return cacheWrapper;
    }


}
