package com.cex0.mobiai.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 21:20
 * @Description: 内存缓存存储(使用ConcurentHashMap存储)。
 */
@Slf4j
public class InMemoryCacheStore extends StringCacheStore{

    /**
     * 清理间隔（毫秒）
     */
    private final static long PERIOD = 60 * 1000;

    /**
     * 保存缓存的容器
     */
    private final static ConcurrentHashMap<String, CacheWrapper<String>> CACHE_CONTAINER = new ConcurrentHashMap<>();

    /**
     * 计时器
     */
    private final Timer timer;

    /**
     * 锁
     */
    private Lock lock = new ReentrantLock();

    public InMemoryCacheStore() {
        // 运行缓存存储清理程序
        timer = new Timer();
        timer.scheduleAtFixedRate(new CacheExpiryCleaner(), 0, PERIOD);
    }

    @Override
    Optional<CacheWrapper<String>> getInternal(String key) {
        Assert.hasText(key, "Cache key must not be blank");

        return Optional.ofNullable(CACHE_CONTAINER.get(key));
    }

    @Override
    void putInternal(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key, "Cache key must not be blank");
        Assert.notNull(cacheWrapper, "Cache wrapper must not be null");

        CacheWrapper<String> putCacheWrapper = CACHE_CONTAINER.put(key, cacheWrapper);

        log.debug("Put [{}] cache result: [{}], original cache wrapper: [{}]", key, putCacheWrapper, cacheWrapper);
    }

    @Override
    Boolean putInternalIfAbsent(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key, "Cache key must not be null");
        Assert.notNull(cacheWrapper, "Cache Wrapper must not be null");

        log.debug("Preparing to put key: [{}], value: [{}]", key, cacheWrapper);

        lock.lock();
        try {
            // 获取之前的值
            Optional<String> valueOptional = get(key);

            if (valueOptional.isPresent()) {
                log.warn("Failed to put the cache, because the key: [{}] has been present already", key);
                return false;
            }

            putInternal(key, cacheWrapper);
            log.debug("Put successfully");
            return true;
        }
        finally {
            lock.unlock();
        }

    }

    @Override
    public void delete(String key) {
        Assert.hasText(key, "Cache key must not be null");

        CACHE_CONTAINER.remove(key);
        log.debug("Remove key: [{}]", key);
    }

    @PreDestroy
    public void preDestroy() {
        log.debug("Cancelling all timer tasks");
        timer.cancel();
        clear();
    }

    private void clear() {
        CACHE_CONTAINER.clear();
    }


    /**
     * 内部类 过期缓存清理
     * @author wodenvyoujiaoshaxiong
     * @date 2020-03-04
     */
    private class CacheExpiryCleaner extends TimerTask {

        @Override
        public void run() {
            CACHE_CONTAINER.keySet().forEach(key ->{
                /*
                  this指的是当前正在访问这段代码的对象,当在内部类中使用this指的就是内部类的对象,
                  为了访问外层类对象,就可以使用外层类名.this来访问,一般也只在这种情况下使用这种
                  形式
                 */
                if (!InMemoryCacheStore.this.get(key).isPresent()) {
                    log.debug("Deleted the cache: [{}] for expiration", key);
                }
            });
        }
    }
}
