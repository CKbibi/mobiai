package com.cex0.mobiai.model.annotation;

import com.cex0.mobiai.model.enums.Mode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/12 21:15
 * @Description: 该注解可以限制某些条件下禁止访问api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableOnCondition {

    @AliasFor("mode")
    Mode value() default Mode.DEMO;

    @AliasFor("value")
    Mode mode() default Mode.DEMO;
}
