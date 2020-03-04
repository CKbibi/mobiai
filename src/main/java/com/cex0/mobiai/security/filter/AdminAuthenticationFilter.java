package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.AuthenticationException;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.security.authentication.AuthenticationImpl;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.context.SecurityContextImpl;
import com.cex0.mobiai.security.support.UserDetail;
import com.cex0.mobiai.security.util.SecurityUtils;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 管理身份验证筛选器。
 *
 * @author Cex0
 * @date 2020/02/28
 */
@Slf4j
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    /**
     * session key
     */
    public final static String ADMIN_SESSION_KEY = "mobiai.admin.session";

    /**
     *  令牌访问缓存前缀
     */
    public final static String TOKEN_ACCESS_CACHE_PREFIX = "mobiai.admin.access.token.";

    /**
     * 令牌刷新缓存前缀
     */
    public final static String TOKEN_REFRESH_CACHE_PREFIX = "mobiai.admin.refresh.token.";

    /**
     * token标题名。
     */
    public final static String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;

    /**
     *  管理令牌查询名称
     */
    public final static String ADMIN_TOKEN_QUERY_NAME = "admin_token";

    private final MobiaiProperties mobiaiProperties;

    private final StringCacheStore cacheStore;

    private final UserService userService;

    public AdminAuthenticationFilter(StringCacheStore cacheStore,
                                     UserService userService,
                                     MobiaiProperties mobiaiProperties,
                                     OptionService optionService) {
        super(mobiaiProperties, optionService);
        this.mobiaiProperties = mobiaiProperties;
        this.cacheStore = cacheStore;
        this.userService = userService;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        Assert.notNull(request, "HTTPServletRequest must not be null");

        // 从头参数中获取
        String token = request.getHeader(ADMIN_TOKEN_HEADER_NAME);

        // 从参数中获取
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(ADMIN_TOKEN_QUERY_NAME);

            log.debug("Got token from parameter: [{} : {}]", ADMIN_TOKEN_QUERY_NAME, token);
        }
        else {
            log.debug("Got token from header: [{} : {}]", ADMIN_TOKEN_HEADER_NAME, token);
        }
        return token;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!mobiaiProperties.isAuthEnabled()) {
            // 设置安全性
            userService.getCurrentUser().ifPresent(user ->
                    SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // 过滤
            filterChain.doFilter(request,response);
            return;
        }

        // 通过request获取token
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            getFailureHandler().onFailure(request, response, new AuthenticationException("未登陆，请登陆后访问"));
        }

        // 通过缓存获取user对象
        Optional<Integer> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            getFailureHandler().onFailure(request, response, new AuthenticationException("Token 已过期或不存在").setErrorData(token));
            return;
        }

        // 获取用户
        User user = userService.getById(optionalUserId.get());

        // 创建detail
        UserDetail userDetail = new UserDetail(user);

        // 设置安全
        SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(userDetail)));

        // filter
        filterChain.doFilter(request, response);
    }

}
