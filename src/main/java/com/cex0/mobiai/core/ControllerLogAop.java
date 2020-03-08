package com.cex0.mobiai.core;

import cn.hutool.extra.servlet.ServletUtil;
import com.cex0.mobiai.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2019/12/26 01:10
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAop {


    @Pointcut("execution(*  *..*.*.controller..*.*(..))")
    public void controller() {
    }

    @Around("controller()")
    public Object controller(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 获取请求属性
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();

        // 请求日志打印(执行方法前log)
        printRequestLog(request, className, methodName, args);
        long stratTime = System.currentTimeMillis();

        // 执行方法
        Object returnObj = joinPoint.proceed();
        printResponseLog(request, className, methodName, returnObj, System.currentTimeMillis() - stratTime);
        return returnObj;
    }

    private void printResponseLog(HttpServletRequest request, String className, String methodName, Object returnObj, long useTime) throws JsonProcessingException {
        if (log.isDebugEnabled()) {
            String returningData = null;
            if (returnObj != null) {
                if (returnObj.getClass().isAssignableFrom(byte[].class)) {
                    returningData = "Binary data";
                } else {
                    returningData = JsonUtils.objectToJson(returnObj);
                }
            }
            log.debug("{}.{} Response: [{}], usage: [{}]ms", className, methodName, returningData, useTime);
        }
    }

    private void printRequestLog(HttpServletRequest request, String clazzName, String methodName, Object[] args) throws JsonProcessingException {
        log.debug("Request URL: [{}], URI: [{}], Request Method: [{}], IP: [{}]",
                request.getRequestURL(),
                request.getRequestURI(),
                request.getMethod(),
                ServletUtil.getClientIP(request));

        if (args == null || log.isDebugEnabled()) {
            return;
        }

        // 出现以下入参还需打印一段
        boolean shouldNotLog = false;
        for (Object arg : args) {
            if (arg == null
                || arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse
                || arg instanceof MultipartFile
                || arg.getClass().isAssignableFrom(MultipartFile[].class)
                ) {
                shouldNotLog = true;
                break;
            }
        }

        if (!shouldNotLog) {
            String requestBody = JsonUtils.objectToJson(args);
            log.debug("{}.{} Parameters: [{}]", clazzName, methodName, requestBody);
        }
    }


}
