package com.cex0.mobiai.security.authentication;

import com.cex0.mobiai.security.support.UserDetail;
import org.springframework.lang.NonNull;

/**
 * 身份认证接口
 * @author Cex0
 */
public interface Authentication {

    /**
     * 获取用户详细信息
     * @return userDetail
     */
    @NonNull
    UserDetail getDetail();
}
