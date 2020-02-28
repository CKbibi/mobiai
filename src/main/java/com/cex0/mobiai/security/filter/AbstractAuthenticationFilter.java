package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.security.handler.AuthenticationFailureHandler;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 身份验证过滤器抽象类
 */
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    protected final AntPathMatcher antPathMatcher;

    protected final MobiaiProperties mobiaiProperties;

    protected final OptionService optionService;

    private AuthenticationFailureHandler authenticationFailureHandler;

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


    public void addExcludeUrlPatterns(@NonNull String ... excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "excludeUrlPatterns must not be null");

        Collections.addAll(this.excludeUrlPatterns, excludeUrlPatterns);
    }
}
