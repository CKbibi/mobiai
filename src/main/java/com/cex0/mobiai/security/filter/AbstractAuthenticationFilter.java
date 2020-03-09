package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.BadRequestException;
import com.cex0.mobiai.exception.ForbiddenException;
import com.cex0.mobiai.exception.MobiaiException;
import com.cex0.mobiai.exception.NotInstallException;
import com.cex0.mobiai.model.enums.Mode;
import com.cex0.mobiai.model.properties.PrimaryProperties;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.handler.AuthenticationFailureHandler;
import com.cex0.mobiai.security.handler.DefaultAuthenticationFailureHandler;
import com.cex0.mobiai.security.service.OneTimeTokenService;
import com.cex0.mobiai.service.OptionService;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import static com.cex0.mobiai.model.support.MobiaiConst.ONE_TIME_TOKEN_QUERY_NAME;
import static com.cex0.mobiai.model.support.MobiaiConst.ONE_TIME_TOKEN_HEADER_NAME;

/**
 * 身份验证过滤器抽象类
 *
 * @author wodenvyoujiaoshaxiong
 * @date 2020/02/28
 */
@Slf4j
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    protected final AntPathMatcher antPathMatcher;

    protected final MobiaiProperties mobiaiProperties;

    protected final OptionService optionService;

    protected final StringCacheStore cacheStore;

    protected final OneTimeTokenService oneTimeTokenService;

    private AuthenticationFailureHandler failureHandler;

    /**
     * 排除的url
     */
    private Set<String> excludeUrlPatterns = new HashSet<>(16);

    AbstractAuthenticationFilter(MobiaiProperties mobiaiProperties,
                                 OptionService optionService,
                                 StringCacheStore cacheStore,
                                 OneTimeTokenService oneTimeTokenService) {
        this.mobiaiProperties = mobiaiProperties;
        this.optionService = optionService;
        this.cacheStore = cacheStore;
        this.oneTimeTokenService = oneTimeTokenService;

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
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest must not be null");

        return excludeUrlPatterns.stream().anyMatch(p -> antPathMatcher.match(p, request.getRequestURI()));
    }


    /**
     * 添加排除的url。
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
                    // 默认身份验证失败的处理
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

        if (!isInstalled && Mode.TEST.equals(mobiaiProperties.getMode())) {
            // 如果没有初始化
            getFailureHandler().onFailure(httpServletRequest, httpServletResponse, new NotInstallException("当前博客未初始化！"));
            return;
        }

        try {
            // 验证一次性token
            if (isSufficientOneTimeToken(httpServletRequest)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            // 认证
            doAuthenticate(httpServletRequest, httpServletResponse, filterChain);
        }
        catch (MobiaiException e) {
            getFailureHandler().onFailure(httpServletRequest, httpServletResponse, e);
        }
        finally {
            SecurityContextHolder.clearContext();
        }
    }


    /**
     * 检查是否有设置一次性的token
     *
     * @param request
     * @return 如果有返回true，否则为false
     */
    private boolean isSufficientOneTimeToken(HttpServletRequest request) {
        final String oneTimeToken = getTokenFromRequest(request, ONE_TIME_TOKEN_QUERY_NAME, ONE_TIME_TOKEN_HEADER_NAME);

        if (StringUtils.isBlank(oneTimeToken)) {
            // 如果未提供一次性令牌，请跳过
            return false;
        }

        String allowedUri = oneTimeTokenService.get(oneTimeToken)
                .orElseThrow(() -> new BadRequestException("The one-time token does not exist").setErrorData(oneTimeToken));

        // 获取uri
        String requestURI = request.getRequestURI();

        if (!StringUtils.equals(requestURI, allowedUri)) {
            // 如果请求的uri和允许的uri不一致
            // TODO 使用蚂蚁路径匹配器可能会更好
            throw new ForbiddenException("The one-time token does not correspond the request uri").setErrorData(oneTimeToken);
        }

        // 返回前删除token
        oneTimeTokenService.revoke(oneTimeToken);

        return true;
    }


    /**
     * 从http servlet请求获取token。
     *
     * @param request
     * @param tokenQueryName
     * @param tokenHeaderName
     * @return 对应的token
     */
    protected String getTokenFromRequest(@NonNull HttpServletRequest request, @NonNull String tokenQueryName, @NonNull String tokenHeaderName) {
        Assert.notNull(request, "HttpServletRequest must not be null");
        Assert.hasText(tokenQueryName, "Token query name must not be blank");
        Assert.hasText(tokenHeaderName, "Token header name must not be blank");

        // 获取头参数
        String accessKey = request.getHeader(tokenHeaderName);

        // 从paramter中获取
        if (StringUtils.isBlank(accessKey)) {
            accessKey = request.getParameter(tokenQueryName);
            log.debug("Got access key from parameter: [{}: {}]", tokenQueryName, accessKey);
        }
        else {
            log.debug("Got access key from header: [{}: {}]", tokenHeaderName, accessKey);
        }

        return accessKey;
    }
}
