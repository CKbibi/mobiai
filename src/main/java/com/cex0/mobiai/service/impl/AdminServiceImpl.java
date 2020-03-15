package com.cex0.mobiai.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.event.logger.LogEvent;
import com.cex0.mobiai.exception.BadRequestException;
import com.cex0.mobiai.exception.NotFoundException;
import com.cex0.mobiai.exception.ServiceException;
import com.cex0.mobiai.mail.MailService;
import com.cex0.mobiai.model.dto.post.EnvironmentDTO;
import com.cex0.mobiai.model.dto.post.StatisticDTO;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.model.enums.LogType;
import com.cex0.mobiai.model.params.LoginParam;
import com.cex0.mobiai.model.params.ResetPasswordParam;
import com.cex0.mobiai.model.properties.EmailProperties;
import com.cex0.mobiai.security.authentication.Authentication;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.token.AuthToken;
import com.cex0.mobiai.security.util.SecurityUtils;
import com.cex0.mobiai.service.AdminService;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.UserService;
import com.cex0.mobiai.util.MobiaiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private OptionService optionService;

    private MailService mailService;


    /**
     * 进行身份验证
     * @param loginParam 登录信息不能为空
     * @return      身份验证信息token
     */
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

    /**
     * 清除token
     */
    @Override
    public void clearToken() {
        // 先从上下文中获取是否有登录记录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new BadRequestException("您还尚未登录，无法注销");
        }

        // 用登录记录中获取用户信息
        User user = authentication.getDetail().getUser();

        // 从缓存中删除token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class).ifPresent(accessToken -> {
            // 删除token
            cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken));
            cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        });

        // 从缓存中删除refresh token
        cacheStore.getAny(SecurityUtils.buildRefreshTokenKey(user), String.class).ifPresent(refreshToken -> {
            cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
            cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));
        });

        // 日志采集
        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_OUT, user.getNickname()));

        log.info("You have been logged out, looking forward to your next visit!");
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {
        // 先验证信息
        cacheStore.getAny("code", String.class).ifPresent(code -> {
            throw new ServiceException("已经获取过验证码了，不能重复获取");
        });

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        // 获取随机数
        String code = RandomUtil.randomNumbers(6);

        log.info("Get reset password code: [{}]", code);

        //  向缓存插入数据key:code value:获取的随机数 超时时间:5分钟
        cacheStore.putAny("code", code, 5, TimeUnit.MINUTES);

        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class, false);

        if (!emailEnabled) {
            throw new ServiceException("未启用 SMTP 服务，无法发送邮件，但是你可以通过系统日志找到验证码");
        }

        // 发送邮件
        String content = "您正在进行密码重置操作，如不是本人操作，请尽快做好相应措施。密码重置验证码如下（五分钟有效）：\n" + code;
        mailService.sendTextMail(param.getEmail(), "找回密码验证码", content);
    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {
        if (StringUtils.isEmpty(param.getCode())) {
            throw new ServiceException("验证码不能为空");
        }

        if (StringUtils.isEmpty(param.getPassword())) {
            throw new ServiceException("密码不能为空");
        }

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        String code = cacheStore.getAny("code", String.class).orElseThrow(() -> new ServiceException("未获取过验证码"));
        if (!code.equals(param.getCode())) {
            throw new ServiceException("验证码不正确");
        }

        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));

        userService.setPassword(user, param.getPassword());

        userService.update(user);

        cacheStore.delete("code");
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
        Assert.hasText(refreshToken, "Refresh Token must not be blank");

        Integer userId = cacheStore.getAny(SecurityUtils.buildTokenRefreshKey(refreshToken), Integer.class)
                .orElseThrow(() -> new BadRequestException("登陆状态已失效，请重新登陆").setErrorData(refreshToken));

        // 获取uesr对象
        User user = userService.getById(userId);

        // 删除所有用户所有token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class)
                .ifPresent(accessToken -> cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken)));
        cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
        cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));

        // 重新设置Token
        return buildAuthToken(user);
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
