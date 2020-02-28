package com.cex0.mobiai.security.util;

import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.security.filter.AdminAuthenticationFilter;
import com.cex0.mobiai.service.AdminService;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 用户验证工具类
 *
 * @author Cex0
 * @date 2020年1月22日
 */
public class SecurityUtils {

    private SecurityUtils() {}

    @NonNull
    public static String buildAccessTokenKey(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        return AdminService.ACCESS_TOKEN_CACHE_PREFIX + user.getId();
    }


    @NonNull
    public static String buildRefreshTokenKey(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        return AdminService.REFRESH_TOKEN_CACHE_PREFIX + user.getId();
    }


    @NonNull
    public static String buildTokenAccessKey(@NonNull String accessToken) {
        Assert.hasText(accessToken, "Access token must not be blank");

        return AdminAuthenticationFilter.TOKEN_ACCESS_CACHE_PREFIX + accessToken;
    }

    @NonNull
    public static String buildTokenRefreshKey(@NonNull String refreshToken) {
        Assert.hasText(refreshToken, "Refresh token must not be blank");

        return AdminAuthenticationFilter.TOKEN_REFRESH_CACHE_PREFIX + refreshToken;
    }
}
