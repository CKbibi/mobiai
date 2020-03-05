package com.cex0.mobiai.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.cache.lock.CacheLock;
import com.cex0.mobiai.event.logger.LogEvent;
import com.cex0.mobiai.event.user.UserUpdatedEvent;
import com.cex0.mobiai.exception.BadRequestException;
import com.cex0.mobiai.exception.ForbiddenException;
import com.cex0.mobiai.exception.NotFoundException;
import com.cex0.mobiai.exception.ServiceException;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.model.enums.LogType;
import com.cex0.mobiai.model.params.UserParam;
import com.cex0.mobiai.repository.UserRepository;
import com.cex0.mobiai.repository.base.BaseRepository;
import com.cex0.mobiai.service.UserService;
import com.cex0.mobiai.service.base.AbstractCrudService;
import com.cex0.mobiai.util.DateUtils;
import com.cex0.mobiai.util.MobiaiUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Userservice 接口实现
 *
 * @author Cex0
 * @date 2020/03/05
 */
@Service
public class UserServiceImpl extends AbstractCrudService<User, Integer> implements UserService {

    private final UserRepository userRepository;

    private final StringCacheStore stringCacheStore;

    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                           StringCacheStore stringCacheStore,
                           ApplicationEventPublisher eventPublisher) {
        super(userRepository);
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.stringCacheStore = stringCacheStore;

    }

    @Override
    public Optional<User> getCurrentUser() {
        List<User> users = listAll();

        if (CollectionUtils.isEmpty(users)) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }


    @Override
    public Optional<User> getByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByUsernameOfNonNull(String username) {
        return getByUserName(username).orElseThrow(() -> new NotFoundException("The username dose not exist").setErrorData(username));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByEmailOfNonNull(String email) {
        return getByEmail(email).orElseThrow(() -> new NotFoundException("The email dose not exist").setErrorData(email));
    }

    @Override
    public User updatePassword(String oldPassword, String newPassword, Integer userId) {
        Assert.hasText(oldPassword, "Old password must not be blank");
        Assert.hasText(newPassword, "New password must mot be blank");
        Assert.notNull(userId, "User id must not be null");

        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("新密码与旧密码不能相同");
        }

        User user = getById(userId);

        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BadRequestException("旧密码输入错误").setErrorData(oldPassword);
        }

        setPassword(user, newPassword);

        User updateUser = update(user);

        eventPublisher.publishEvent(new LogEvent(this, updateUser.getId().toString(), LogType.PASSWORD_UPDATED,
                MobiaiUtils.desensitize(oldPassword, 2, 1)));
        return null;
    }

    @Override
    public User createBy(UserParam userParam) {
        Assert.notNull(userParam, "User param info must not be null");

        User user = userParam.convertTo();

        setPassword(user, userParam.getPassword());

        return create(user);
    }

    @Override
    public void mustNotExpire(User user) {
        Assert.notNull(user, "User must not be null");

        Date now = DateUtils.now();
        if (user.getExpireTime() != null && user.getExpireTime().after(now)) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(user.getExpireTime().getTime() - now.getTime());
            throw new ForbiddenException("账户已被停用， 请" + MobiaiUtils.timeFormat(seconds) + "后重试").setErrorData(seconds);
        }
    }

    @Override
    public boolean passwordMatch(User user, String plainPassword) {
        Assert.notNull(user, "User must not be null");

        return !StringUtils.isBlank(plainPassword) && BCrypt.checkpw(plainPassword, user.getPassword());
    }

    @Override
    @CacheLock
    public User create(User user) {
        if (count() != 0) {
            throw new BadRequestException("当前博客已有用户");
        }

        User createUser = super.create(user);

        eventPublisher.publishEvent(new UserUpdatedEvent(this, user.getId()));

        return createUser;
    }

    @Override
    public User update(User user) {
        User update = super.update(user);

        eventPublisher.publishEvent(new LogEvent(this, user.getId().toString(), LogType.PROFILE_UPDATED,
                user.getUsername()));
        eventPublisher.publishEvent(new UserUpdatedEvent(this, user.getId()));

        return update;
    }

    @Override
    public void setPassword(User user, String plainPassword) {
        Assert.notNull(user, "User must not be null");
        Assert.hasText(plainPassword, "Plain password must not be blank");

        user.setPassword(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
    }

    @Override
    public boolean verifyUser(String username, String password) {
        User user = getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));

        return user.getUsername().equals(username) && user.getEmail().equals(password);
    }
}
