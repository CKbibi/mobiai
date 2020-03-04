package com.cex0.mobiai.cache.lock;



import com.cex0.mobiai.exception.FrequentAccessException;
import com.cex0.mobiai.exception.ServiceException;
import com.cex0.mobiai.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import com.cex0.mobiai.cache.StringCacheStore;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 22:44
 * @Description: 缓存锁注解实现
 */
@Slf4j
@Aspect
@Configuration
public class CacheLockInterceptor {

    private final static String CACHE_LOCK_PREFOX = "cache_lock_";

    private final static String CACHE_LOCK_VALUE = "locked";

    private final StringCacheStore cacheStore;

    public CacheLockInterceptor(StringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Around("@annotation(com.cex0.mobiai.cache.lock.CacheLock)")
    public Object interceptCacheLock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        log.debug("Starting locking: [{}]", methodSignature.toString());

        // 获取缓存锁
        CacheLock cacheLock = methodSignature.getMethod().getAnnotation(CacheLock.class);

        // 生成锁的key
        String cacheLockKey = buildCacheLockKey(cacheLock, joinPoint);

        log.debug("Build lock key: [{}]", cacheLockKey);

        try {
            Boolean cacheResult = cacheStore.putIfAbsent(cacheLockKey, CACHE_LOCK_VALUE, cacheLock.expired(), cacheLock.timeUnit());

            if (cacheResult == null) {
                throw new ServiceException("Unknow reason of cache" + cacheLockKey).setErrorData(cacheLockKey);
            }

            if (!cacheResult) {
                throw new FrequentAccessException("访问过于频繁，请稍后再试！").setErrorData(cacheLockKey);
            }

            // 继续执行方法
            return joinPoint.proceed();
        }
        finally {
            if (cacheLock.autoDelete()) {
                cacheStore.delete(cacheLockKey);
                log.debug("Delete the cache lock: [{}]", cacheLock);
            }
        }
    }

    private String buildCacheLockKey(@NonNull CacheLock cacheLock, @NonNull ProceedingJoinPoint joinPoint) {
        Assert.notNull(cacheLock, "Cache Lock must not be null");
        Assert.notNull(joinPoint, "JoinPoing must not be null");

        // 获取到方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 生成缓存锁的key
        StringBuilder cacheKeyStringBuilder = new StringBuilder(CACHE_LOCK_PREFOX);
        String delimiter = cacheLock.delimiter();

        if (StringUtils.isNotBlank(cacheLock.prefix())) {
            cacheKeyStringBuilder.append(cacheLock.prefix());
        }
        else {
            cacheKeyStringBuilder.append(methodSignature.getMethod().toString());
        }

        // 处理缓存锁key生成
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            log.debug("Parameter annotation[{}] = {}", i, parameterAnnotations[i]);

            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation annotation = parameterAnnotations[i][i];
                log.debug("Parameter annotation[{}][{}]", i, j, annotation);
                if (annotation instanceof CacheParam) {
                    // 获取当前参数
                    Object arg = joinPoint.getArgs()[i];
                    log.debug("Cache param args: [{}]", arg);

                    // 添加到缓存key
                    cacheKeyStringBuilder.append(delimiter).append(arg.toString());
                }
            }
        }

        if (cacheLock.traceRequest()) {
            // 添加到请求信息中
            cacheKeyStringBuilder.append(delimiter).append(ServletUtils.getRequestIp());
        }

        return cacheKeyStringBuilder.toString();
    }
}
