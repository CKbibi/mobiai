package com.cex0.mobiai.filter;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/9 22:59
 * @Description: 日志过滤器
 */
@Slf4j
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String remoteAddr = ServletUtil.getClientIP(httpServletRequest);

        log.debug("");
        log.debug("Starting url: [{}], method: [{}], IP: [{}]", httpServletRequest.getRequestURL(),
                httpServletRequest.getMethod(), remoteAddr);

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 执行
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        log.debug("Ending url: [{}], method: [{}], IP: [{}], status: [{}] usage: [{}] ms",
                httpServletRequest.getRequestURL(),
                httpServletRequest.getMethod(),
                remoteAddr,
                httpServletResponse.getStatus(),
                System.currentTimeMillis() - startTime);
        log.debug("");
    }
}
