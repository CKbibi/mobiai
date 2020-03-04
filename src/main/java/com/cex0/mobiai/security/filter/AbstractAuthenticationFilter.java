package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.NotInstallException;
import com.cex0.mobiai.model.properties.PrimaryProperties;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.handler.AuthenticationFailureHandler;
import com.cex0.mobiai.security.handler.DefaultAuthenticationFailureHandler;
import com.cex0.mobiai.service.OptionService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 身份验证过滤器抽象类
 *
 * @author wodenvyoujiaoshaxiong
 * @date 2020/02/28
 */
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    protected final AntPathMatcher antPathMatcher;

    protected final MobiaiProperties mobiaiProperties;

    protected final OptionService optionService;

    private AuthenticationFailureHandler failureHandler;

    /**
     * 排除url模式
     */
    private Set<String> excludeUrlPatterns = new HashSet<>(2);

    protected AbstractAuthenticationFilter(MobiaiProperties mobiaiProperties,
                                           OptionService optionService) {
        this.mobiaiProperties = mobiaiProperties;
        this.optionService = optionService;

        antPathMatcher = new AntPathMatcher();
    }


    /**
     * 通过request获取token
     *
     * @param request   httpServletRequest不能为空
     * @return          token or null
     */
    @Nullable
    protected abstract String getTokenFromRequest(@NonNull HttpServletRequest request);


    protected abstract void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Assert.notNull(request, "HttpServletRequest must not be null");

        return excludeUrlPatterns.stream().anyMatch(p -> antPathMatcher.match(p, request.getServletPath()));
    }


    /**
     * 添加排除url模式。
     *
     * @param excludeUrlPatterns exclude urls
     */
    public void addExcludeUrlPatterns(@NonNull String ... excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "excludeUrlPatterns must not be null");

        Collections.addAll(this.excludeUrlPatterns, excludeUrlPatterns);
    }


    /**
     * 获取排除url模式。
     * @return
     */
    @NonNull
    public Set<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }


    /**
     * 设置排除url模式
     *
     * @param excludeUrlPatterns
     */
    public void setExcludeUrlPatterns(@NonNull Collection<String> excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "excludeUrlPatterns must not be null");

        this.excludeUrlPatterns = new HashSet<>(excludeUrlPatterns);
    }


    /**
     *
     * 获取身份验证失败处理程序。（默认值：@DefaultAuthenticationFailureHandler）
     *
     * @return  身份验证失败程序
     */
    protected AuthenticationFailureHandler getFailureHandler() {
        if (failureHandler == null) {
            synchronized (this) {
                if (failureHandler == null) {
                    // Create default authentication failure handler
                    DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
                    failureHandler.setProductionEnv(mobiaiProperties.isProductionEnv());

                    this.failureHandler = failureHandler;
                }
            }
        }

        return failureHandler;
    }


    /**
     * 设置身份认证失败程序
     *
     * @param failureHandler
     */
    public void setFailureHandler(@NonNull AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must not be null");

        this.failureHandler = failureHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //检查是否初始化
        Boolean isInstalled = optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false);

        if (!isInstalled) {
            // 如果没有初始化
            getFailureHandler().onFailure(httpServletRequest, httpServletResponse, new NotInstallException("当前博客未初始化！"));
            return;
        }

        if (shouldNotFilter(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        try{
            // 认证
            doAuthenticate(httpServletRequest, httpServletResponse, filterChain);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
