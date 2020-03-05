package com.cex0.mobiai.service.impl;

import cn.hutool.core.lang.Validator;
import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.event.logger.LogEvent;
import com.cex0.mobiai.exception.BadRequestException;
import com.cex0.mobiai.exception.NotFoundException;
import com.cex0.mobiai.model.dto.post.EnvironmentDTO;
import com.cex0.mobiai.model.dto.post.StatisticDTO;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.model.enums.LogType;
import com.cex0.mobiai.model.params.LoginParam;
import com.cex0.mobiai.model.params.ResetPasswordParam;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.token.AuthToken;
import com.cex0.mobiai.security.util.SecurityUtils;
import com.cex0.mobiai.service.AdminService;
import com.cex0.mobiai.service.UserService;
import com.cex0.mobiai.util.MobiaiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.concurrent.TimeUnit;


/**
 *  Admin Service
 * @author Cex0
 * @date 2020/03/05
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private StringCacheStore cacheStore;


    @Override
    public AuthToken authenticate(LoginParam loginParam) {
        Assert.notNull(loginParam, "LoginParam must not be null");

        String username = loginParam.getUsername();

        String mismatchTip = "用户名或者密码不正确!";

        final User user;

        try {
            // 判断是否用户名登录还是邮箱登录
            user = Validator.isEmail(username) ?
                    userService.getByEmailOfNonNull(username) : userService.getByUsernameOfNonNull(username);
        } catch (NotFoundException e) {
            log.error("Failed to find user by name: " + username, e);
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        userService.mustNotExpire(user);

        // 判断密码是否一致
        if (!userService.passwordMatch(user, loginParam.getPassword())) {
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        if (SecurityContextHolder.getContext().isAuthenticated()) {
            throw new BadRequestException("您已登录，请不要重复登录");
        }

        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_IN, user.getNickname()));

        return buildAuthToken(user);
    }

    @Override
    public void clearToken() {

    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {

    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {

    }

    @Override
    public StatisticDTO getCount() {
        return null;
    }

    @Override
    public EnvironmentDTO getEnvironments() {
        return null;
    }

    @Override
    public AuthToken refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void updateAdminAssets() {

    }

    @Override
    public String getSpringLogs() {
        return null;
    }


    /**
     * 创建token
     *
     * @param user
     * @return
     */
    @NonNull
    private AuthToken buildAuthToken(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        AuthToken token = new AuthToken();
        token.setAccessToken(MobiaiUtils.randomUUIDWithoutDash());
        token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
        token.setRefreshToken(MobiaiUtils.randomUUIDWithoutDash());

        cacheStore.putAny(SecurityUtils.buildAccessTokenKey(user), token.getAccessToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);
        cacheStore.putAny(SecurityUtils.buildRefreshTokenKey(user), token.getRefreshToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        cacheStore.putAny(SecurityUtils.buildTokenAccessKey(token.getAccessToken()), user.getId(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(SecurityUtils.buildTokenRefreshKey(token.getRefreshToken()), user.getId(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        return token;
    }
}
