package com.cex0.mobiai.service;

import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.model.params.UserParam;
import com.cex0.mobiai.service.base.CrudService;
import org.springframework.lang.NonNull;
import com.cex0.mobiai.exception.NotFoundException;
import com.cex0.mobiai.exception.ForbiddenException;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface UserService extends CrudService<User, Integer> {

    /**
     * 登录失败计数密钥。
     */
    String LOGIN_FAILURE_COUNT_KEY = "login.failure.count";


    /**
     * 最大登录次数
     */
    int MAX_LOGIN_TRY = 5;


    /**
     * 锁定时间
     */
    int LOCK_MINUTES = 10;


    /**
     * 获取当前用户
     *
     * @return Optional的User对象
     */
    @NonNull
    Optional<User> getCurrentUser();


    /**
     * 通过用户名获取用户
     *
     * @param username  用户明不能为空
     * @return          Optional的User对象
     */
    @NonNull
    Optional<User> getByUserName(@NonNull String username);


    /**
     * 按用户名获取非空用户
     *
     * @param username  用户明不能为空
     * @return          user对象
     * @throws NotFoundException 当用户名不存在时引发
     */
    @NonNull
    User getUsernameOfNonNull(@NonNull String username);


    /**
     * 通过邮箱获取用户
     *
     * @param email email不能为空
     * @return      Optional的User对象
     */
    @NonNull
    User getByEmail(@NonNull String email);


    /**
     * 按邮箱获取非空用户
     *
     * @param email email不能为空
     * @return      user对象
     * @throws NotFoundException 当用户名不存在时引发
     */
    @NonNull
    User getByEmailOfNonNull(@NonNull String email);


    /**
     * 重制密码
     * @param oldPassword   旧密码不能为空或空字符串
     * @param newPassword   新密码不能为空或空字符串
     * @param userId        用户id不能为空
     * @return
     */
    User updatePassword(@NonNull String oldPassword, @NonNull String newPassword, @NonNull Integer userId);


    /**
     * 用户注册
     *
     * @param userParam
     * @return
     */
    @NonNull
    User createBy(@NonNull UserParam userParam);


    /**
     * 用户不能过期。
     *
     * @param user
     * @throws ForbiddenException 如果给定用户已过期，则抛出异常
     */
    void mustNotExpire(@NonNull User user);


    /**
     * 检查密码是否与用户密码匹配
     * @param user
     * @param plainPassword
     * @return 如果给定的密码与用户密码匹配，则为true；否则为false
     */
    boolean passwordMatch(@NonNull User user, @Nullable String plainPassword);


    /**
     * 设置密码
     *
     * @param user
     * @param plainPassword
     */
    void setPassword(@NonNull User user, @NonNull String plainPassword);


    /**
     * 验证用户的电子邮件和用户名
     *
     * @param username
     * @param password
     * @return
     */
    boolean verifyUser(@NonNull String username, @NonNull String password);
}
