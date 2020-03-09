package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.AuthenticationException;
import com.cex0.mobiai.exception.ForbiddenException;
import com.cex0.mobiai.model.properties.ApiProperties;
import com.cex0.mobiai.model.properties.CommentProperties;
import com.cex0.mobiai.security.service.OneTimeTokenService;
import com.cex0.mobiai.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.cex0.mobiai.model.support.MobiaiConst.API_ACCESS_KEY_HEADER_NAME;
import static com.cex0.mobiai.model.support.MobiaiConst.API_ACCESS_KEY_QUERY_NAME;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 16:39
 * @Description:
 */
@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter{

    private final OptionService optionService;

    public ApiAuthenticationFilter(MobiaiProperties mobiaiProperties,
                                   OptionService optionService,
                                   StringCacheStore cacheStore,
                                   OneTimeTokenService oneTimeTokenService) {
        super(mobiaiProperties, optionService, cacheStore, oneTimeTokenService);

        this.optionService = optionService;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, API_ACCESS_KEY_QUERY_NAME, API_ACCESS_KEY_HEADER_NAME);
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!mobiaiProperties.isAuthEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从option中获取api是否启用
        Boolean apiEnabled = optionService.getByPropertyOrDefault(ApiProperties.API_ENABLED, Boolean.class, false);

        if (!apiEnabled) {
            throw new ForbiddenException("API has been disabled by blogger currently");
        }

        // 获取accessKey
        String accessKey = getTokenFromRequest(request);

        if (StringUtils.isBlank(accessKey)) {
            // 如果accessKey不存在
            throw new AuthenticationException("Missing API access key");
        }

        // 获取option中的accessKey
        Optional<String> optionalAccessKey = optionService.getByProperty(ApiProperties.API_ACCESS_KEY, String.class);

        if (!optionalAccessKey.isPresent()) {
            // 如果未设置访问密钥
            throw new AuthenticationException("API access key hasn't been set by blogger");
        }

        if (!StringUtils.equals(accessKey, optionalAccessKey.get())) {
            // 密钥不匹配
            throw new AuthenticationException("API access key is mismatch").setErrorData(accessKey);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = super.shouldNotFilter(request);

        if (antPathMatcher.match("/api/content/*/comments", request.getServletPath())) {
            Boolean commentApiEnabled = optionService.getByPropertyOrDefault(CommentProperties.API_ENABLED, Boolean.class, true);
            if (!commentApiEnabled) {
                // 如果注释api被禁用
                result = false;
            }
        }
        return result;
    }
}
