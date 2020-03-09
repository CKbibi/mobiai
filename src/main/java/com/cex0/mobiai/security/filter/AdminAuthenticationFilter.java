package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.AuthenticationException;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.security.authentication.AuthenticationImpl;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.context.SecurityContextImpl;
import com.cex0.mobiai.security.service.OneTimeTokenService;
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
import static com.cex0.mobiai.model.support.MobiaiConst.ADMIN_TOKEN_HEADER_NAME;
import static com.cex0.mobiai.model.support.MobiaiConst.ADMIN_TOKEN_QUERY_NAME;

/**
 * 管理身份验证筛选器。
 *
 * @author Cex0
 * @date 2020/02/28
 */
@Slf4j
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    private final MobiaiProperties mobiaiProperties;

    private final UserService userService;

    public AdminAuthenticationFilter(StringCacheStore cacheStore,
                                     UserService userService,
                                     MobiaiProperties mobiaiProperties,
                                     OptionService optionService,
                                     OneTimeTokenService oneTimeTokenService) {
        super(mobiaiProperties, optionService, cacheStore, oneTimeTokenService);

        this.mobiaiProperties = mobiaiProperties;
        this.userService = userService;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!mobiaiProperties.isAuthEnabled()) {
            // 设置安全性
            userService.getCurrentUser().ifPresent(user ->
                    SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // 执行
            filterChain.doFilter(request,response);
            return;
        }

        // 通过request获取token
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登陆，请登陆后访问");
        }

        // 通过缓存获取user对象
        Optional<Integer> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已过期或不存在").setErrorData(token);
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
