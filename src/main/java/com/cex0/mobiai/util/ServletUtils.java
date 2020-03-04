package com.cex0.mobiai.util;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 23:27
 * @Description:
 */
public class ServletUtils {

    private ServletUtils() {

    }

    /**
     * 获取当前的http servlet请求。
     *
     * @return httpservlet请求
     */
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);
    }


    /**
     * 获取请求ip
     *
     * @return 获取ip地址或者空
     */
    public static String getRequestIp() {
        return getCurrentRequest().map(ServletUtil::getClientIP).orElse(null);
    }


    /**
     * 获取请求头
     *
     * @param header 请求头名
     * @return
     */
    public static String getHeaderIgnoreCase(String header) {
        return getCurrentRequest().map(request -> ServletUtil.getHeaderIgnoreCase(request, header)).orElse(null);
    }
}
