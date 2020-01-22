package com.cex0.mobiai.security.support;

import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.exception.AuthenticationException;
import lombok.*;

/**
 * 用户详细信息
 * @author Cex0
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    private User user;

    /**
     * 获取用户信息
     * @return  用户对象
     * @throws  AuthenticationException throws if the user is null
     */
    public User getUser() {
        return user;
    }


    /**
     * set用户
     * @param user  用户对象
     */
    public void setUser(User user) {
        this.user = user;
    }
}
