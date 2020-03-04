package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.exception.AuthenticationException;
import com.cex0.mobiai.exception.ForbiddenException;
import com.cex0.mobiai.model.properties.ApiProperties;
import com.cex0.mobiai.model.properties.CommentProperties;
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

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 16:39
 * @Description:
 */
@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter{

    public final static String API_ACCESS_KEY_HEADER_NAME = "API-" + HttpHeaders.AUTHORIZATION;

    public final static String API_ACCESS_KEY_QUERY_NAME = "api_access_key";

    private final OptionService optionService;

    public ApiAuthenticationFilter(MobiaiProperties mobiaiProperties,
                                   OptionService optionService) {
        super(mobiaiProperties, optionService);
        this.optionService = optionService;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        Assert.notNull(request, "Http servlet request must not be null");

        // 从头参数中获取
        String accessKey = request.getHeader(API_ACCESS_KEY_HEADER_NAME);

        // 从param中获取
        if (StringUtils.isBlank(accessKey)) {
            accessKey = request.getParameter(API_ACCESS_KEY_QUERY_NAME);

            log.debug("Got access key from parameter: [{}: {}]", API_ACCESS_KEY_QUERY_NAME, accessKey);
        }
        else {
            log.debug("Got access key from header: [{}: {}]", API_ACCESS_KEY_HEADER_NAME, accessKey);
        }

        return accessKey;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!mobiaiProperties.isAuthEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        Boolean apiEnabled = optionService.getByPropertyOrDefault(ApiProperties.API_ENABLED, Boolean.class, false);

        if (!apiEnabled) {
            getFailureHandler().onFailure(request, response, new ForbiddenException("API has been disabled by blogger currently"));
            return;
        }

        String accessKey = getTokenFromRequest(request);

        if (StringUtils.isBlank(accessKey)) {
            getFailureHandler().onFailure(request, response, new AuthenticationException("Missing API access key"));
            return;
        }

        Optional<String> optionalAccessKey = optionService.getByProperty(ApiProperties.API_ACCESS_KEY, String.class);

        if (!optionalAccessKey.isPresent()) {
            getFailureHandler().onFailure(request, response, new AuthenticationException("API access key hasn't been set by blogger"));
            return;
        }

        if (!StringUtils.equals(accessKey, optionalAccessKey.get())) {
            getFailureHandler().onFailure(request, response, new AuthenticationException("API access key is mismatch"));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean result = super.shouldNotFilter(request);

        if (antPathMatcher.match("/api/content/*/comments", request.getServletPath())) {
            Boolean commentApiEnabled = optionService.getByPropertyOrDefault(CommentProperties.API_ENABLED, Boolean.class, true);
            if (!commentApiEnabled) {
                // If the comment api is disabled
                result = false;
            }
        }
        return result;
    }
}
