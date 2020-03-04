package com.cex0.mobiai.cache.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 21:59
 * @Description: 缓存锁注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * 缓存前缀，默认""
     * @return 缓存前缀
     */
    @AliasFor("value")
    String prefix() default "";

    /**
     * 前缀的别名，默认""
     * @return
     */
    @AliasFor("prefix")
    String value() default "";

    /**
     * 过期时间，默认5
     * @return
     */
    long expired() default 5;

    /**
     * 时间单位，默认秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 分隔符，默认:
     * @return
     */
    String delimiter() default ":";

    /**
     * 是否自动删除，默认是
     * @return
     */
    boolean autoDelete() default true;

    /**
     * 是否跟踪请求信息，默认否
     * @return
     */
    boolean traceRequest() default false;
}
