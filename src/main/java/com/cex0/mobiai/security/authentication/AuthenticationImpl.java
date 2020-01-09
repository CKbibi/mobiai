package com.cex0.mobiai.security.authentication;

import com.cex0.mobiai.security.support.UserDetail;

/**
 * 身份认证实现
 *
 * @author Cex0
 */
public class AuthenticationImpl implements Authentication {

    private final UserDetail userDetail;

    public AuthenticationImpl(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public UserDetail getDetail() {
        return userDetail;
    }
}
