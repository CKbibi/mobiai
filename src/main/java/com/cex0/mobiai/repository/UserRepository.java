package com.cex0.mobiai.repository;

import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.repository.base.BaseRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * 用户储存库
 */
public interface UserRepository extends BaseRepository<User, Integer> {

    /**
     * 通过用户名获取用户信息
     * @param username  用户名
     * @return          用户信息
     */
    @NonNull
    Optional<User> findByUsername(@NonNull String username);


    /**
     * 通过邮箱获取用户信息
     * @param email     邮箱账号
     * @return          用户信息
     */
    @NonNull
    Optional<User> findByEmail(@NonNull String email);
}
