package com.cex0.mobiai.security.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 上下文安全控制
 * @author Cex0
 * @date 2020-01-09
 */
public class SecurityContextHolder {

    private final static ThreadLocal<SecurityContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private SecurityContextHolder() {}


    @NonNull
    public static SecurityContext getContext() {
        SecurityContext context = CONTEXT_HOLDER.get();
        if (context == null) {
            context = createEmptyContext();
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }


    /**
     * 设置上下文安全
     *
     * @param context 安全上下文
     */
    public static void setContext(@Nullable SecurityContext context) {
        CONTEXT_HOLDER.set(context);
    }


    /**
     * 清除上下文安全
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }


    /**
     * 创建一个空的上下文安全
     *
     * @return 一个空的上下文
     */
    @NonNull
    private static SecurityContext createEmptyContext() {
        return new SecurityContextImpl(null);
    }
}
