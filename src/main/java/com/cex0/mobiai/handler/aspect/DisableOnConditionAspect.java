package com.cex0.mobiai.handler.aspect;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.ForbiddenException;
import com.cex0.mobiai.model.annotation.DisableOnCondition;
import com.cex0.mobiai.model.enums.Mode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/12 21:19
 * @Description:
 */
@Aspect
@Slf4j
@Component
public class DisableOnConditionAspect {

    private final MobiaiProperties mobiaiProperties;

    public DisableOnConditionAspect(MobiaiProperties mobiaiProperties) {
        this.mobiaiProperties = mobiaiProperties;
    }

    @Pointcut("@annotation(com.cex0.mobiai.model.annotation.DisableOnCondition)")
    public void pointcut() {

    }

    @Around("pointcut() && @annotation(disableApi)")
    public Object around(ProceedingJoinPoint joinPoint,
                         DisableOnCondition disableApi) throws Throwable {
        Mode mode = disableApi.mode();
        if (mobiaiProperties.getMode().equals(mode)) {
            throw new ForbiddenException("禁止访问");
        }

        return joinPoint.proceed();
    }
}
